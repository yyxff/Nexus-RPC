package com.github.yyxff.nexusrpc.registry;

import com.github.yyxff.nexusrpc.registry.RegistryJDK.RegistryHandlerJDK;
import com.github.yyxff.nexusrpc.registry.RegistryJDK.ServiceRegistryJDK;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServiceRegistryMain {
    public static void main(String[] args){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
            RegistryHandler requestHandler = new RegistryHandlerJDK(new ServiceRegistryJDK());
            server.createContext("/register", requestHandler);
            server.createContext("/lookup", requestHandler);
            System.out.println("Server started at " + new InetSocketAddress(8888));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("server error: ", e);
        }
    }
}
