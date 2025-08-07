package com.github.yyxff.nexusrpc.registry.messagestruct;

import java.net.InetSocketAddress;
import java.util.List;

public class RegistryResponse {
    public boolean success;
    public List<InetSocketAddress> serverList;
}