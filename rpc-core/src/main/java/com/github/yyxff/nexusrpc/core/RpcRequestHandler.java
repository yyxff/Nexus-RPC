package com.github.yyxff.nexusrpc.core;

import com.github.yyxff.nexusrpc.common.RpcResponse;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

// TODO: multiplexing

/**
 * This instance will be run in another thread with event loop
 * So we need a future to wait the response
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final InetSocketAddress serverAddress;
    private final CircuitBreaker circuitBreaker;
    private final Dispatcher dispatcher;

    private static final Logger logger = Logger.getLogger(RpcRequestHandler.class.getName());

    public RpcRequestHandler(InetSocketAddress server,
                             CircuitBreaker circuitBreaker,
                             Dispatcher dispatcher) {
        this.serverAddress = server;
        this.circuitBreaker = circuitBreaker;
        this.dispatcher = dispatcher;
    }

    /**
     * 1. Get result future by id in response
     * 2. Complete it then remove it
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        String id = msg.getResponseID();
        // Complete future
        dispatcher.complete(id, msg);
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
}
