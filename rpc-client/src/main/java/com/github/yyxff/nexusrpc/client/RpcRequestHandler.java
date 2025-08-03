package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import com.github.yyxff.nexusrpc.core.CircuitBreaker;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

// TODO: multiplexing

/**
 * This instance will be run in another thread with event loop
 * So we need a future to wait the response
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcResponse rpcResponse;
    private CompletableFuture<RpcResponse> future;

    private final InetSocketAddress serverAddress;
    private final RpcRequest request;
    private final CircuitBreaker circuitBreaker;
    private static final Logger logger = Logger.getLogger(RpcRequestHandler.class.getName());

    public RpcRequestHandler(InetSocketAddress server, RpcRequest rpcRequest, CircuitBreaker circuitBreaker) {
        this.serverAddress = server;
        this.request = rpcRequest;
        this.circuitBreaker = circuitBreaker;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.rpcResponse = msg;
        future.complete(msg);
        // Close connection, removed after long connection implemented
        ctx.close();
    }

    /**
     * This will be call when timeout
     * But this should be used to stay alive in the future
     * timeout change be checked by another timer task
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                logger.severe("WRITER_IDLE");
            } else if (event.state() == IdleState.READER_IDLE) {
                // Service may be unavailable for a while. Record it.
                // TODO record it in circuit breaker
                circuitBreaker.fail(serverAddress);
                logger.info("READER_IDLE");
                ctx.close();
            } else if (event.state() == IdleState.ALL_IDLE) {
                // TODO send heartbeat
                // ctx.writeAndFlush(new PingRequest());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    RpcResponse getResponse() {
        return rpcResponse;
    }

    public void addFuture(CompletableFuture<RpcResponse> future) {
        this.future = future;
    }
}
