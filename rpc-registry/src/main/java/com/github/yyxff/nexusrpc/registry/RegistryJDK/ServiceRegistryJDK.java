package com.github.yyxff.nexusrpc.registry.RegistryJDK;

import com.github.yyxff.nexusrpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryJDK implements ServiceRegistry {

    private final Map<String, InetSocketAddress> serviceMap = new ConcurrentHashMap<>();

    /**
     * Register a server by service name
     * Todo: save a server list for a service name
     * @param interfaceName
     * @param address
     */
    @Override
    public void register(String interfaceName, InetSocketAddress address) {
        serviceMap.put(interfaceName, address);
    }

    /**
     * Look up a server list by service name
     * If not found, return null
     * @param interfaceName
     * @return
     */
    @Override
    public InetSocketAddress lookup(String interfaceName) {
        if (!serviceMap.containsKey(interfaceName)) {
            return null;
        }
        return serviceMap.get(interfaceName);
    }

}
