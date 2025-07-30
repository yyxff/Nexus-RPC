package com.github.yyxff.nexusrpc.core;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalancer {

    InetSocketAddress selectServer(String serviceName, List<InetSocketAddress> inetSocketAddressList);


}
