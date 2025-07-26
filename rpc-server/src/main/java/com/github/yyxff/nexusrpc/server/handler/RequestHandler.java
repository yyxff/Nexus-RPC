package com.github.yyxff.nexusrpc.server.handler;

import com.alibaba.nacos.api.remote.response.Response;
import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;

public class RequestHandler {

    RpcResponse handle(RpcRequest rpcRequest) {
        String interfaceName = rpcRequest.getInterfaceName();

        // todo: do the real call here
        // serviceRegistry.get

        // todo: populate rpcResponse
        RpcResponse rpcResponse = new RpcResponse(
                rpcRequest.getRequestID(),
                null,
                null
        );
        return rpcResponse;
    }
}
