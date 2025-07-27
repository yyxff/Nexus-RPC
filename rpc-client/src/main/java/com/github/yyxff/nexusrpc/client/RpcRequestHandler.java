package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import com.gitub.yyxff.nexusrpc.core.RpcDecoder;
import com.gitub.yyxff.nexusrpc.core.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcResponse rpcResponse;
    private CompletableFuture<RpcResponse> future;
    private static final Logger logger = Logger.getLogger(RpcRequestHandler.class.getName());


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.rpcResponse = msg;
        future.complete(msg);
    }

    RpcResponse getResponse() {
        return rpcResponse;
    }

    public void addFuture(CompletableFuture<RpcResponse> future) {
        this.future = future;
    }
}
