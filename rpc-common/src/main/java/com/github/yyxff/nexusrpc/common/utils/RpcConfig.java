package com.github.yyxff.nexusrpc.common.utils;

import java.util.List;

public class RpcConfig {
    public RegistryConfig registry;
    // public ServerConfig server;
    // public ClientConfig client;

    public static class RegistryConfig {
        public String type;
        public NacosConfig nacos;
        public JDKRegistryConfig JDK;
        public EtcdConfig etcd;
    }

    public static class NacosConfig {
        public String address;
        public String namespace;
        public String username;
        public String password;
    }

    public static class JDKRegistryConfig {
        public String address;
    }

    public static class EtcdConfig {
        public String address;
    }

    // public static class ServerConfig {
    //     public int port;
    //     public String host;
    //     public String appName;
    // }
    //
    // public static class ClientConfig {
    //     public String appName;
    //     public int timeout;
    // }
}