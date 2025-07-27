package com.github.yyxff.nexusrpc.server;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map from interfaceName to actual service instance
 */
public class ServiceMap {

    private final Map<String, ServiceProvider> serviceMap = new ConcurrentHashMap<>();

    // This func should be called before register
    public void addService(String interfaceName, ServiceProvider serviceProvider){
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
        ServiceProvider serviceProvider = serviceMap.get(interfaceName);
        Method method = serviceProvider.getClass().getMethod(methodName, paramTypes);
        Object result = method.invoke(serviceProvider, args);
        return result;
    }
}
