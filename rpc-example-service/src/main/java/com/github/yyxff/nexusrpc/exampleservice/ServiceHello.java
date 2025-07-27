package com.github.yyxff.nexusrpc.exampleservice;

import com.github.yyxff.nexusrpc.server.ServiceProvider;

public class ServiceHello implements ServiceProvider {

    String hello(String name){
        return "Hello " + name + "! This is Nexus RPC Server!";
    }
}
