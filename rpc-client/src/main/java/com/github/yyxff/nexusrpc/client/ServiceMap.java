package com.github.yyxff.nexusrpc.client;

import com.github.yyxff.nexusrpc.registry.RegistryClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMap {

    private final Map<String, List<InetSocketAddress>> serviceMap = new ConcurrentHashMap<>();
    private final RegistryClient registry;

    public ServiceMap(RegistryClient registry) {
        this.registry = registry;
    }

    public List<InetSocketAddress> get(String serviceName) {
        if (!serviceMap.containsKey(serviceName)) {
            lookUpFromRegistry(serviceName);
        }
        return serviceMap.get(serviceName);
    }

    private void lookUpFromRegistry(String serviceName) {
        try{
            List<InetSocketAddress> serverList = registry.lookup(serviceName);
            if (!serviceMap.containsKey(serviceName)) {
                serviceMap.put(serviceName, new ArrayList<>());
            }
            serviceMap.get(serviceName)
                      .addAll(serverList);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
