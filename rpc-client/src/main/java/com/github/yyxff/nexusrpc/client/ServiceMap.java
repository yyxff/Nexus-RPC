package com.github.yyxff.nexusrpc.client;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.github.yyxff.nexusrpc.registry.RegistryResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMap {

    private final Map<String, List<InetSocketAddress>> serviceMap = new ConcurrentHashMap<>();
    private final LookUpHandler lookUpHandler = new LookUpHandler();

    public List<InetSocketAddress> get(String serviceName) {
        if (!serviceMap.containsKey(serviceName)) {
            lookUpFromRegistry(serviceName);
        }
        return serviceMap.get(serviceName);
    }

    private void lookUpFromRegistry(String serviceName) {
        try{
            RegistryResponse registryResponse = lookUpHandler.lookUp(serviceName);
            if (!serviceMap.containsKey(serviceName)) {
                serviceMap.put(serviceName, new ArrayList<>());
            }
            serviceMap.get(serviceName)
                      .addAll(registryResponse.serverList);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
