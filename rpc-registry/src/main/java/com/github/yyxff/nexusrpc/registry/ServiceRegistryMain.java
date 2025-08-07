package com.github.yyxff.nexusrpc.registry;

import com.github.yyxff.nexusrpc.registry.serviceregistry.ServiceRegistryJDK;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public class ServiceRegistryMain {
    public static void main(String[] args){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
            Properties properties = new Properties();
            // properties.setProperty("serverAddr", "127.0.0.1:8848"); // Nacos 服务地址
            // properties.setProperty("namespace", "public");
            // properties.setProperty("grpc.client.enable", "false");
            RegistryHandler requestHandler = new RegistryHandlerHTTP(new ServiceRegistryJDK());
            // RegistryHandler requestHandler = new RegistryHandlerHTTP(new NacosRegistryClient(properties));
            server.createContext("/register", requestHandler);
            server.createContext("/lookup", requestHandler);
            System.out.println("Server started at " + new InetSocketAddress(8888));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("server error: ", e);
        }
    }
}
