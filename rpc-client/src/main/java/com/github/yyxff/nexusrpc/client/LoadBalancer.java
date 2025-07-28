package com.github.yyxff.nexusrpc.client;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalancer {

    InetSocketAddress selectServer(List<InetSocketAddress> inetSocketAddressList);


}
