package com.github.yyxff.nexusrpc.exampleservice;

import com.github.yyxff.nexusrpc.server.RpcServer;
import com.github.yyxff.nexusrpc.server.ServiceMap;
import com.github.yyxff.nexusrpc.server.handler.RegisterHandler;
import java.util.Collection;
import java.util.logging.*;

public class ServerMain {

    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    // Register service and Start rpc server
    public static void main(String[] args) throws Exception {

//        setupLogger();

        ServiceMap serviceMap = new ServiceMap();
        addServices(serviceMap);
        logger.info("Added services to map");

        RegisterHandler registerHandler = new RegisterHandler();
        registerServices(registerHandler, serviceMap.getInterfaces());
        logger.info("Registered services to registry");

        logger.info("Start Rpc server");
        RpcServer rpcServer = new RpcServer(serviceMap);
        rpcServer.start();
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
    private static void registerServices(RegisterHandler registerHandler, Collection<String> interfaces) {
        for (String interfaceName : interfaces){
            try{
                logger.info("Registering service " + interfaceName);
                registerHandler.registry(interfaceName);
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
