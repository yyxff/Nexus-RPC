package com.github.yyxff.nexusrpc.client.loadbalancer;

import com.github.yyxff.nexusrpc.client.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load balancer with policy Round-Robin
 * It stores the state for last used server
 */
public class LoadBalancerRoundRobin implements LoadBalancer {

    /**
     * Mapping from service name to last used index
     * So we can add one and return the next server
     */
    private final Map<String, Integer> serviceToLastUsed= new HashMap();

    @Override
    public InetSocketAddress selectServer(String serviceName, List<InetSocketAddress> serverList) {
        if (!serviceToLastUsed.containsKey(serviceName)) {
            serviceToLastUsed.put(serviceName, 0);
        }
        int lastUsed = serviceToLastUsed.get(serviceName);
        lastUsed += 1;
        if (lastUsed >= serverList.size()) {
            lastUsed = 0;
        }
        serviceToLastUsed.put(serviceName, lastUsed);
        return serverList.get(lastUsed);
    }
}
