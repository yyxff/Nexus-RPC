package com.github.yyxff.nexusrpc.common;

public class RpcResponse {
    private long responseID;
    private Object result;
    private Class<?> resultType;

    public long getResponseID() {
        return responseID;
    }

    public Object getResult() {
        return result;
    }

    public Class<?> getResultType() {
        return resultType;
    }
}


