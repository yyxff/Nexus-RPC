package com.github.yyxff.nexusrpc.registry;

import com.github.yyxff.nexusrpc.registry.messagestruct.RegistryResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface RegistryHandler extends HttpHandler {

    public void handle(HttpExchange exchange) throws IOException;

    public void respond(HttpExchange exchange, RegistryResponse response) throws IOException;
}
