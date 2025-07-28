package com.github.yyxff.nexusrpc.exampleclient;

import com.github.yyxff.nexusrpc.client.RpcClient;
import com.github.yyxff.nexusrpc.client.RpcClientProxy;
import com.github.yyxff.nexusrpc.client.ServiceMap;
import com.github.yyxff.nexusrpc.client.loadbalancer.LoadBalancerRandom;

import java.io.IOException;
import java.util.logging.Logger;
public class ClientMain {

    private static final Logger logger = Logger.getLogger(ClientMain.class.getName());

    public static void main(String[] args) throws IOException {
        RpcClient rpcClient = new RpcClient(new ServiceMap(), new LoadBalancerRandom());
        RpcClientProxy proxy = new RpcClientProxy(rpcClient);
        ServiceHello serviceHello = proxy.getProxy(ServiceHello.class);

        String result = serviceHello.hello("nexus rpc client");
        logger.info("Get response from server: "+result);
    }
}
