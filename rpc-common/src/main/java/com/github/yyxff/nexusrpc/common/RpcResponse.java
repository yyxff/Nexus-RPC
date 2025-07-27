package com.github.yyxff.nexusrpc.common;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private String responseID;
    private Object result;
    private Class<?> resultType;

    public RpcResponse(String responseID,
                       Object result,
                       Class<?> resultType) {
        this.responseID = responseID;
        this.result = result;
        this.resultType = resultType;
    }

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


