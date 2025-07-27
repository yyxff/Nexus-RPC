package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.registry.RegistryResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMap {

    private final Map<String, InetSocketAddress> serviceMap = new ConcurrentHashMap<>();
    private final LookUpHandler lookUpHandler = new LookUpHandler();

    InetSocketAddress get(String serviceName) {
        if (!serviceMap.containsKey(serviceName)) {
            lookUpFromRegistry(serviceName);
        }
        return serviceMap.get(serviceName);
    }

    private void lookUpFromRegistry(String serviceName) {
        try{
            RegistryResponse registryResponse = lookUpHandler.lookUp(serviceName);
            serviceMap.put(serviceName, new InetSocketAddress(registryResponse.host, registryResponse.port));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
