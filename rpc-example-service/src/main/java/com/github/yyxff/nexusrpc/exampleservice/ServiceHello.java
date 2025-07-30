package com.github.yyxff.nexusrpc.exampleservice;

import com.github.yyxff.nexusrpc.server.ServiceProvider;

public class ServiceHello implements ServiceProvider {

    public String hello(String name){
        try{
            Thread.sleep(6000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "Hello " + name + "! This is Nexus RPC Server!";
    }
}
