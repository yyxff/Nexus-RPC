package com.github.yyxff.nexusrpc.registry;

import java.net.InetSocketAddress;
import java.util.List;

public interface RegistryClient {

    void register(String interfaceName, InetSocketAddress address);

    List<InetSocketAddress> lookup(String interfaceName);
}
