package com.github.yyxff.nexusrpc.common.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.github.yyxff.nexusrpc.core.Serializer;
import com.github.yyxff.nexusrpc.core.serializers.SerializerJDK;
import com.github.yyxff.nexusrpc.registry.RegistryClient;
import com.github.yyxff.nexusrpc.registry.registryclient.NacosRegistryClient;
import com.github.yyxff.nexusrpc.registry.registryclient.RegistryClientJDK;

import java.util.Properties;

public class ComponentFactory {

    public static RegistryClient getRegistry(RpcConfig config){
        switch (config.registry.type) {
            case "nacos":{
                try {
                    Properties properties = new Properties();
                    properties.setProperty("serverAddr", config.registry.nacos.address);
                    properties.setProperty("namespace", "public");
                    return new NacosRegistryClient(properties);
                } catch (NacosException e) {
                    throw new RuntimeException("Failed to get nacos registry",e);
                }
            }
            case "JDK":{
                return new RegistryClientJDK();
            }
            default:{return null;}
        }
    }

    public static Serializer getSerializer(RpcConfig config){
        switch (config.serializer.type) {
            case "JDK": return new SerializerJDK();
            default: return null;
        }
    }

}
