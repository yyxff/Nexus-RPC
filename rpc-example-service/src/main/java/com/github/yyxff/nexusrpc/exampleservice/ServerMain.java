package com.github.yyxff.nexusrpc.exampleservice;

import com.alibaba.nacos.api.exception.NacosException;
import com.github.yyxff.nexusrpc.registry.RegistryClient;
import com.github.yyxff.nexusrpc.registry.registryclient.NacosRegistryClient;
import com.github.yyxff.nexusrpc.registry.registryclient.RegistryClientJDK;
import com.github.yyxff.nexusrpc.server.RpcServer;
import com.github.yyxff.nexusrpc.server.ServiceMap;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.*;

public class ServerMain {

    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    // Register service and Start rpc server
    public static void main(String[] args) throws Exception {

        // setupLogger();
        // try {
        ServiceMap serviceMap = new ServiceMap();
        addServices(serviceMap);
        logger.info("Added services to map");

        // RegistryClient registry = new RegistryClientJDK();
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "127.0.0.1:8848"); // Nacos 服务端地址和端口
        properties.setProperty("namespace", "public");
        RegistryClient registry = new NacosRegistryClient(properties);
        registerServices(registry, serviceMap.getInterfaces());
        for (String serviceName : serviceMap.getInterfaces()) {
            registry.register(serviceName, new InetSocketAddress("127.0.0.1", 8080));
        }
        logger.info("Registered services to registry");

        logger.info("Start Rpc server");
        RpcServer rpcServer = new RpcServer(serviceMap, 8080);
        rpcServer.start();
        // }catch (NacosException e){
        //     e.printStackTrace();
        // }
    }

    /**
     * Add service providers to |serviceMap|
     * @param serviceMap
     */
    private static void addServices(ServiceMap serviceMap) {
        // Service Hello
        serviceMap.addService(ServiceHello.class.getSimpleName(), new ServiceHello());
    }

    /**
     * Register all services to registry
     */
    private static void registerServices(RegistryClient registry, Collection<String> interfaces) {
        for (String interfaceName : interfaces){
            try{
                logger.info("Registering service " + interfaceName);
                registry.register(interfaceName, new InetSocketAddress("127.0.0.1", 8080));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void setupLogger() {
        // 获取全局根 Logger
        Logger rootLogger = LogManager.getLogManager().getLogger("");

        // 设置根 Logger 级别（决定最低日志级别）
        rootLogger.setLevel(Level.FINE);

        // 移除默认的 Handler
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }

        // 创建新的 ConsoleHandler 并设置级别和格式化器
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINE);
        // consoleHandler.setFormatter(new SingleLineFormatter()); // 你自定义的 Formatter

        // 添加 Handler 到根 Logger
        rootLogger.addHandler(consoleHandler);
    }
}
