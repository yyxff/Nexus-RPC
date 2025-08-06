package com.github.yyxff.nexusrpc.client;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import java.util.logging.Logger;

public class RpcClientProxy implements InvocationHandler {

    /**
     * The client that sends request to registry and rpc server
     */
    private final RpcClient rpcClient;
    private static final Logger logger = Logger.getLogger(RpcClientProxy.class.getName());


    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this
        );
    }

    /**
     * All method called on this proxy instance will trigger this
     * |invoke()| and pass the method, args to this func
     *
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest(
                UUID.randomUUID().toString(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                args,
                method.getParameterTypes()
        );
        RpcResponse response = rpcClient.remoteCall(rpcRequest.getInterfaceName(), rpcRequest);
//        logger.info(response.toString());
        return response.getResult();
    }
}
