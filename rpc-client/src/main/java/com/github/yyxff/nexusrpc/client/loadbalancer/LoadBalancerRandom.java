package com.github.yyxff.nexusrpc.client.loadbalancer;

import com.github.yyxff.nexusrpc.client.LoadBalancer;
import com.github.yyxff.nexusrpc.client.ServiceMap;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class LoadBalancerRandom implements LoadBalancer {

    @Override
    public InetSocketAddress selectServer(List<InetSocketAddress> inetSocketAddressList) {
        return inetSocketAddressList.get(new Random().nextInt(inetSocketAddressList.size()));
    }
}
