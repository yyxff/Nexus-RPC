package com.github.yyxff.nexusrpc.common;

public class RpcRequest {
    private long requestID;
    private String interfaceName;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramsType;

    public long getRequestID() {
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
