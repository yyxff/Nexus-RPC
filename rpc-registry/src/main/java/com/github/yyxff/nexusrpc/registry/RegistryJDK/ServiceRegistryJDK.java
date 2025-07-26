package com.github.yyxff.nexusrpc.registry.RegistryJDK;

import com.github.yyxff.nexusrpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryJDK implements ServiceRegistry {

    private final Map<String, InetSocketAddress> serviceMap = new ConcurrentHashMap<>();

    @Override
    public void register(String interfaceName, InetSocketAddress address) {
        serviceMap.put(interfaceName, address);
    }

    @Override
    public InetSocketAddress lookup(String interfaceName) {
        if (!serviceMap.containsKey(interfaceName)) {
            return null;
        }
        return serviceMap.get(interfaceName);
    }

}
