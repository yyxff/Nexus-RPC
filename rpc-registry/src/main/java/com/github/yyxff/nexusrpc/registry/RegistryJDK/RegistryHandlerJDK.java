package com.github.yyxff.nexusrpc.registry.RegistryJDK;

import com.github.yyxff.nexusrpc.registry.ServiceRegistry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.Registry;
import java.rmi.registry.RegistryHandler;

class RegisterRequest {
    public String serviceName;
    public String host;
    public int port;
}

class RegisterResponse {
    boolean success;
    public String host;
    public int port;
}

public class RegistryHandlerJDK implements HttpHandler {

    private final ServiceRegistry serviceRegistry;
    // Converter between json and object
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegistryHandlerJDK(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        InputStream requestBody = exchange.getRequestBody();
        RegisterRequest req = objectMapper.readValue(requestBody, RegisterRequest.class);

        if (path.equals("/register")) {
            serviceRegistry.register(req.serviceName, new InetSocketAddress(req.host, req.port));

            // Response
            RegisterResponse response = new RegisterResponse();
            response.success = true;
            // Respond
            respond(exchange, response);

        } else if (path.equals("/lookup")) {
            serviceRegistry.lookup(req.serviceName);

            // Response
            RegisterResponse response = new RegisterResponse();
            response.success = true;
            response.host = req.host;
            response.port = req.port;
            // Respond
            respond(exchange, response);
        }
    }


    private void respond(HttpExchange exchange, RegisterResponse response) throws IOException {
        // Convert to json
        String jsonResponse = objectMapper.writeValueAsString(response);
        // Set header
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        // Write
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}

