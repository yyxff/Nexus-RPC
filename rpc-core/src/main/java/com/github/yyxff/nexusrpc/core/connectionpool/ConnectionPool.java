package com.github.yyxff.nexusrpc.core.connectionpool;

import com.github.yyxff.nexusrpc.core.*;
import com.github.yyxff.nexusrpc.core.serializers.SerializerJDK;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ConnectionPool {
    private final Map<InetSocketAddress, CopyOnWriteArrayList<PooledChannel>> pool = new ConcurrentHashMap<>();

    private final NioEventLoopGroup group;
    private final CircuitBreaker circuitBreaker;
    private final Dispatcher dispatcher;
    private final Serializer serializerIn;
    private final Serializer serializerOut;
    private final int timeout = 5;
    private final int allIdleTime = 10;

    public ConnectionPool(NioEventLoopGroup group,
                          CircuitBreaker circuitBreaker,
                          Dispatcher dispatcher,
                          Serializer serializerIn,
                          Serializer serializerOut) {
        this.group = group;
        this.circuitBreaker = circuitBreaker;
        this.dispatcher = dispatcher;
        this.serializerIn = serializerIn;
        this.serializerOut = serializerOut;
    }

    // TODO: select a available channel(check if using)
    public PooledChannel getChannel(InetSocketAddress serverAddress) {
        if (!pool.containsKey(serverAddress)) {
            List<PooledChannel> list = pool.computeIfAbsent(serverAddress, k -> new CopyOnWriteArrayList<>());
            Channel newChannel = createChannel(serverAddress, group, circuitBreaker);
            list.add(new PooledChannel(newChannel));
        }
        // Select a random channel
        List<PooledChannel> channels = pool.get(serverAddress);
        int index = ThreadLocalRandom.current().nextInt(channels.size());
        return channels.get(index);
    }

    public Channel createChannel(InetSocketAddress serverAddress, NioEventLoopGroup group, CircuitBreaker circuitBreaker) {
        Bootstrap bootstrap = new Bootstrap();
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler(serverAddress, circuitBreaker, dispatcher);

        try{
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(serializerIn)); // 解码器
                            p.addLast(new RpcEncoder(serializerOut)); // Java对象编码器
//                            p.addLast(new IdleStateHandler(timeout, timeout, allIdleTime, TimeUnit.SECONDS));
                            p.addLast(rpcRequestHandler); // 处理响应
                        }
                    });

            ChannelFuture future = bootstrap.connect(serverAddress.getHostName(), serverAddress.getPort()).sync();
            return future.channel();
        }catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
