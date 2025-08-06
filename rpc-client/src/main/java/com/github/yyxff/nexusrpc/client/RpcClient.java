package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import com.github.yyxff.nexusrpc.core.*;
import com.github.yyxff.nexusrpc.core.connectionpool.ConnectionPool;
import com.github.yyxff.nexusrpc.core.connectionpool.PooledChannel;
import com.github.yyxff.nexusrpc.core.serializers.SerializerJDK;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class RpcClient {

    // Mapping from service name to server list
    private final ServiceMap serviceMap;
    // Load balancer
    private final LoadBalancer loadBalancer;
    // Circuit breaker
    private final CircuitBreaker circuitBreaker = new CircuitBreaker();
    // Dispatcher
    private final Dispatcher dispatcher = new Dispatcher();
    // ConnectionPool
    private final ConnectionPool connectionPool;

    // timeout for read or write: 5s
    private final int timeout = 5;

    /**
     * timeout if both read and write is idle: 10s
     * if happened, send a heartbeat package to server to check alive
     */
    private final int allIdleTime = 10;

    // Logger
    private static final Logger logger = Logger.getLogger(RpcClient.class.getName());

    public RpcClient(ServiceMap serviceMap, LoadBalancer loadBalancer) {
        this.serviceMap = serviceMap;
        this.loadBalancer = loadBalancer;
        this.connectionPool = new ConnectionPool(new NioEventLoopGroup(), circuitBreaker, dispatcher);
    }

    /**
     * 1. Get available service
     * 2. Send remote call by netty
     * @param serviceName
     * @param request
     * @return Response of remote call
     */
    RpcResponse remoteCall(String serviceName, RpcRequest request) {
        InetSocketAddress serverAddress = getAvailableServer(serviceName);
        logger.info("Selected address: " + serverAddress);

        PooledChannel channel = connectionPool.getChannel(serverAddress);
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        dispatcher.registerFuture(request.getRequestID(), resultFuture);
        // Send request
        channel.writeAndFlush(request);
        logger.info("waiting for future");
        try {
            return resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
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
