package com.github.yyxff.nexusrpc.exampleservice;

import com.github.yyxff.nexusrpc.server.ServiceProvider;

public class ServiceHello implements ServiceProvider {

    String hello(){
        return "Hello from Nexus RPC Service!";
    }
}
