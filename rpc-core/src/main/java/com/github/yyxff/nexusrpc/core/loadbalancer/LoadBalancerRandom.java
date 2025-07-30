package com.github.yyxff.nexusrpc.core.loadbalancer;


import com.github.yyxff.nexusrpc.core.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class LoadBalancerRandom implements LoadBalancer {

    @Override
    public InetSocketAddress selectServer(String serviceName, List<InetSocketAddress> inetSocketAddressList) {
        return inetSocketAddressList.get(new Random().nextInt(inetSocketAddressList.size()));
    }
}
