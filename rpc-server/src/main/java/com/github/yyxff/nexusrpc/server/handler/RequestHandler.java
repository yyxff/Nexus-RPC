package com.github.yyxff.nexusrpc.server.handler;

import com.github.yyxff.nexusrpc.core.messagestruct.RpcRequest;
import com.github.yyxff.nexusrpc.core.messagestruct.RpcResponse;
import com.github.yyxff.nexusrpc.server.ServiceMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

public class RequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final ServiceMap serviceMap;
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());


    public RequestHandler(ServiceMap serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {

        logger.info("service called: "+rpcRequest.getInterfaceName() + ", "+rpcRequest.getMethodName());
        // Call the actual service
        Object result = serviceMap.invoke(
                rpcRequest.getInterfaceName(),
                rpcRequest.getMethodName(),
                rpcRequest.getParamsType(),
                rpcRequest.getParams());

        // Populate rpcResponse
        RpcResponse rpcResponse = new RpcResponse(
                rpcRequest.getRequestID(),
                result,
                null
        );

        // Write it back
        ctx.writeAndFlush(rpcResponse);
    }
}
