package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class RpcClient {

    private final ServiceMap serviceMap = new ServiceMap();
    private static final Logger logger = Logger.getLogger(RpcClient.class.getName());


    RpcResponse sendRequest(String serviceName, RpcRequest request){
        InetSocketAddress inetSocketAddress = serviceMap.get(serviceName);
        logger.info("get server address: " + inetSocketAddress);
        return null;
    }

}
