package com.github.yyxff.nexusrpc.core.messagestruct;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    // ID to retrieve result future from dispatcher
    private String responseID;

    // Returned result from real function
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


