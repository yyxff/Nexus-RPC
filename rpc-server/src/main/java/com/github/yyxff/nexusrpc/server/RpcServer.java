package com.github.yyxff.nexusrpc.server;

import com.github.yyxff.nexusrpc.server.handler.RequestHandler;
import com.github.yyxff.nexusrpc.core.RpcDecoder;
import com.github.yyxff.nexusrpc.core.RpcEncoder;
import com.github.yyxff.nexusrpc.core.serializers.SerializerJDK;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.logging.*;

public class RpcServer {

    private final int port = 8080;
    private final ServiceMap serviceMap;
    private static final Logger logger = Logger.getLogger(RpcServer.class.getName());


    public RpcServer(ServiceMap serviceMap) {
        this.serviceMap = serviceMap;
    }

    /**
     * Start TCP server with netty framework
     * and self-defined protocol
     * and self-defined encoder/decoder
     * and self-defined serializer
     * @throws Exception
     */
    public void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            logger.setLevel(Level.ALL);
            logger.info("RPC server started on port " + port);
            System.out.println("RPC server started on port " + port);
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcDecoder(new SerializerJDK()));
                            p.addLast(new RpcEncoder(new SerializerJDK()));
                            p.addLast(new RequestHandler(serviceMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
