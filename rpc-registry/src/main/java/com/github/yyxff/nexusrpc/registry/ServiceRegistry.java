package com.github.yyxff.nexusrpc.registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {

    void register(String interfaceName, InetSocketAddress address);

    InetSocketAddress lookup(String interfaceName);
}
