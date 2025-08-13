package com.github.yyxff.nexusrpc.core;

import com.github.yyxff.nexusrpc.core.messagestruct.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Store all result future waiting to be completed
 */
public class Dispatcher {

    private final Map<String, CompletableFuture<RpcResponse>> idToResult = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(Dispatcher.class.getName());

    /**
     * Register a result future to map by |id|
     * It should be completed and removed when response is ready
     * @param id
     * @param future
     */
    public void registerFuture(String id, CompletableFuture<RpcResponse> future) {
        idToResult.put(id, future);
    }

    /**
     * Complete a future with |response| by |id|, then remove it from map
     * @param id
     * @param response
     */
    public void complete(String id, RpcResponse response) {
        if (!idToResult.containsKey(id)) {
            throw new IllegalStateException("No such id: " + id);
        }
        CompletableFuture<RpcResponse> future = idToResult.remove(id);
        future.complete(response);
    }
}
