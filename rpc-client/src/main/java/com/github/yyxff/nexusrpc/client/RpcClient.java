package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import com.github.yyxff.nexusrpc.core.CircuitBreaker;
import com.github.yyxff.nexusrpc.core.LoadBalancer;
import com.github.yyxff.nexusrpc.core.RpcDecoder;
import com.github.yyxff.nexusrpc.core.RpcEncoder;
import com.github.yyxff.nexusrpc.core.serializers.SerializerJDK;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RpcClient {

    private final ServiceMap serviceMap;
    private final LoadBalancer loadBalancer;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker();

    // timeout for read or write: 5s
    private final int timeout = 5;

    /**
     * timeout if both read and write is idle: 10s
     * if happened, send a heartbeat package to server to check alive
     */
    private final int allIdleTime = 10;

    private static final Logger logger = Logger.getLogger(RpcClient.class.getName());

    public RpcClient(ServiceMap serviceMap, LoadBalancer loadBalancer) {
        this.serviceMap = serviceMap;
        this.loadBalancer = loadBalancer;
    }

    RpcResponse sendRequest(String serviceName, RpcRequest request) {
        InetSocketAddress serverAddress = getAvailableServer(serviceName);
        logger.info("Selected address: " + serverAddress);

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
            RpcRequestHandler rpcRequestHandler = new RpcRequestHandler(serverAddress, request, circuitBreaker);
            rpcRequestHandler.addFuture(resultFuture);

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(new SerializerJDK())); // 解码器
                            p.addLast(new RpcEncoder(new SerializerJDK())); // Java对象编码器
                            p.addLast(new IdleStateHandler(timeout, timeout, allIdleTime, TimeUnit.SECONDS));
                            p.addLast(rpcRequestHandler); // 处理响应
                        }
                    });

            ChannelFuture future = bootstrap.connect(serverAddress.getHostName(), serverAddress.getPort()).sync();
            Channel channel = future.channel();

            channel.writeAndFlush(request).sync(); // 发送请求
//            channel.closeFuture().sync(); // 等待关闭
            logger.info("waiting for future");
            return resultFuture.get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get available server by serviceName
     * 1. Get all servers from serviceMap
     * 2. Filter servers by circuitBreaker
     * 3. Select a server by loadBalancer
     * @param serviceName
     * @return
     */
    InetSocketAddress getAvailableServer(String serviceName) {
        List<InetSocketAddress> serverList = serviceMap.get(serviceName);
        List<InetSocketAddress> availableServerList = new ArrayList<>(circuitBreaker.filter(serverList));
        logger.info("Available server list: " + availableServerList);
        return loadBalancer.selectServer(serviceName, availableServerList);
    }
}
