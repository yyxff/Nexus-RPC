package com.github.yyxff.nexusrpc.exampleclient;

import com.alibaba.nacos.api.exception.NacosException;
import com.github.yyxff.nexusrpc.client.RpcClient;
import com.github.yyxff.nexusrpc.client.RpcClientProxy;
import com.github.yyxff.nexusrpc.client.ServiceMap;
import com.github.yyxff.nexusrpc.core.loadbalancer.LoadBalancerRandom;
import com.github.yyxff.nexusrpc.registry.registryclient.NacosRegistryClient;
import com.github.yyxff.nexusrpc.registry.registryclient.RegistryClientJDK;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ClientMain {

    private static final Logger logger = Logger.getLogger(ClientMain.class.getName());

    public static void main(String[] args) throws IOException {
        try {
            // RpcClient rpcClient = new RpcClient(new ServiceMap(new RegistryClientJDK()), new LoadBalancerRandom());
            Properties properties = new Properties();
            properties.setProperty("serverAddr", "127.0.0.1:8848"); // Nacos 服务端地址和端口
            properties.setProperty("namespace", "public");
            RpcClient rpcClient = new RpcClient(new ServiceMap(new NacosRegistryClient(properties)), new LoadBalancerRandom());
            RpcClientProxy proxy = new RpcClientProxy(rpcClient);

            // Get proxy instance of specific service
            ServiceHello serviceHello = proxy.getProxy(ServiceHello.class);

            // Do remote call (in sync mode)
            String result = serviceHello.hello("nexus rpc client");
            logger.info("Get response from server: "+result);

            result = serviceHello.hello("nexus rpc client");
        logger.info("Get response from server: "+result);
        }catch (NacosException e){
            e.printStackTrace();
            return;
        }
    }
}
