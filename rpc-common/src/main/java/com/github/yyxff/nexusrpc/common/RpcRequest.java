package com.github.yyxff.nexusrpc.common;

import java.io.Serializable;

public class RpcRequest implements Serializable {
    private String requestID;
    private String interfaceName;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramsType;

    public RpcRequest(String requestID,
                      String interfaceName,
                      String methodName,
                      Object[] params,
                      Class<?>[] paramsType) {
        this.requestID = requestID;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.params = params;
        this.paramsType = paramsType;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public Class<?>[] getParamsType() {
        return paramsType;
    }
}
