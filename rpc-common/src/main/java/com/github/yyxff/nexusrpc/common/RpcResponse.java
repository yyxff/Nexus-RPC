package com.github.yyxff.nexusrpc.common;

public class RpcResponse {
    private String responseID;
    private Object result;
    private Class<?> resultType;

    public String getResponseID() {
        return responseID;
    }

    public Object getResult() {
        return result;
    }

    public Class<?> getResultType() {
        return resultType;
    }
}


