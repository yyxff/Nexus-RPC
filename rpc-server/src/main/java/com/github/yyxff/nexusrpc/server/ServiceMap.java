package com.github.yyxff.nexusrpc.server;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Map from interfaceName to actual service instance
 */
public class ServiceMap {

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(ServiceMap.class.getName());


    // This func should be called before register
    public void addService(String interfaceName,  Object serviceProvider){
        logger.info("Added service: "+interfaceName);
        serviceMap.put(interfaceName, serviceProvider);
    }

    public Collection<String> getInterfaces() {
        return serviceMap.keySet();
    }

    // Can I make it generic?
    public Object invoke(String interfaceName, String methodName,  Class<?>[] paramTypes, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (!serviceMap.containsKey(interfaceName)) {
            throw new NoSuchMethodException("No such service: " + interfaceName);
        }
        logger.info(
                "Found service: "+interfaceName
                +", method: "+methodName
                +", param types: "+ Arrays.toString(paramTypes)
                +", args: "+Arrays.toString(args));
        Object serviceProvider = serviceMap.get(interfaceName);
        Method method = serviceProvider.getClass().getMethod(methodName, paramTypes);
        logger.info("Found method: "+method);
        Object result = method.invoke(serviceProvider, args);
        logger.info("Got result: "+result);
        return result;
    }
}
