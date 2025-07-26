package com.github.yyxff.nexusrpc.registry;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServiceRegistryMain {
    public static void main(String[] args){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        } catch (IOException e) {
            throw new RuntimeException("server error: ", e);
        }
    }
}
