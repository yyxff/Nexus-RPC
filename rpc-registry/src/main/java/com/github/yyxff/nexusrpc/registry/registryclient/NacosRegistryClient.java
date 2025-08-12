package com.github.yyxff.nexusrpc.registry.registryclient;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.yyxff.nexusrpc.registry.RegistryClient;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class NacosRegistryClient implements RegistryClient {

    private final NamingService namingService;

    public NacosRegistryClient(Properties properties) throws NacosException {
        this.namingService = NamingFactory.createNamingService(properties);
    }

    @Override
    public void register(String serviceName, InetSocketAddress address) {
        try{
            // Use API provided by nacos server
            namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        }catch (NacosException e) {
            throw new RuntimeException("Failed to register service in Nacos", e);
        }
    }

    @Override
    public List<InetSocketAddress> lookup(String serviceName) {
        try {
            // Use API provided by nacos server
            List<Instance> instances = namingService.getAllInstances(serviceName);
            return instances.stream()
                    .map(i -> new InetSocketAddress(i.getIp(), i.getPort()))
                    .collect(Collectors.toList());
        }catch (NacosException e) {
            throw new RuntimeException("Failed to lookup service in Nacos", e);
        }
    }
}
