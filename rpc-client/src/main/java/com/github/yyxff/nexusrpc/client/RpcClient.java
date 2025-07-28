package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import com.gitub.yyxff.nexusrpc.core.RpcDecoder;
import com.gitub.yyxff.nexusrpc.core.RpcEncoder;
import com.gitub.yyxff.nexusrpc.core.serializers.SerializerJDK;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class RpcClient {

    private final ServiceMap serviceMap;
    private final LoadBalancer loadBalancer;
    private final RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
    private static final Logger logger = Logger.getLogger(RpcClient.class.getName());

    public RpcClient(ServiceMap serviceMap, LoadBalancer loadBalancer) {
        this.serviceMap = serviceMap;
        this.loadBalancer = loadBalancer;
    }

    RpcResponse sendRequest(String serviceName, RpcRequest request) {
        List<InetSocketAddress> serverList = serviceMap.get(serviceName);
        InetSocketAddress serverAddress = loadBalancer.selectServer(serverList);
        logger.info("Server list: " + serverList);
        logger.info("Selected address: " + serverAddress);

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
            rpcRequestHandler.addFuture(resultFuture);

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(new SerializerJDK())); // 解码器
                            p.addLast(new RpcEncoder(new SerializerJDK())); // Java对象编码器
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
}
