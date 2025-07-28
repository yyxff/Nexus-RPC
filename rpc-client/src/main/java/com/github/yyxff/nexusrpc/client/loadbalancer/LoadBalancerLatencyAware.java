package com.github.yyxff.nexusrpc.client.loadbalancer;

import com.github.yyxff.nexusrpc.client.LoadBalancer;
import com.github.yyxff.nexusrpc.common.utils.WeightedRandom;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancerLatencyAware implements LoadBalancer {

    private final Map<String, Map<InetSocketAddress, Double>> serviceToLatency = new HashMap();

    @Override
    public InetSocketAddress selectServer(String serviceName, List<InetSocketAddress> serverList) {

        List<Double> weights = new ArrayList();
        for (InetSocketAddress server : serverList) {
            if (serviceToLatency.containsKey(serviceName) || serviceToLatency.get(serviceName).containsKey(server)) {
                // weight = 1000 / latency(ms)
                weights.add(1000 / serviceToLatency.get(serviceName).get(server));
            } else {
                // If no record data, use default weight
                weights.add(1.0);
            }
        }

        return WeightedRandom.select(serverList, weights);
    }

    // void update();
}
