package com.github.yyxff.nexusrpc.registry.RegistryJDK;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.github.yyxff.nexusrpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryJDK implements ServiceRegistry {

    private final Map<String, List<InetSocketAddress>> serviceMap = new ConcurrentHashMap<>();

    /**
     * Register a server by service name
     * Todo: save a server list for a service name
     * @param serviceName
     * @param address
     */
    @Override
    public void register(String serviceName, InetSocketAddress address) {
        if (!serviceMap.containsKey(serviceName)) {
            serviceMap.put(serviceName, new ArrayList<>());
        }
        serviceMap.get(serviceName).add(address);
    }

    /**
     * Look up a server list by service name
     * If not found, return null
     * @param serviceName
     * @return
     */
    @Override
    public List<InetSocketAddress> lookup(String serviceName) {
        if (!serviceMap.containsKey(serviceName) || serviceMap.get(serviceName).size() == 0) {
            return null;
        }
        return serviceMap.get(serviceName);
    }

}
