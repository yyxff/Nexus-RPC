package com.github.yyxff.nexusrpc.registry.RegistryJDK;

import com.github.yyxff.nexusrpc.registry.RegistryHandler;
import com.github.yyxff.nexusrpc.registry.RegistryRequest;
import com.github.yyxff.nexusrpc.registry.RegistryResponse;
import com.github.yyxff.nexusrpc.registry.ServiceRegistry;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;


public class RegistryHandlerJDK implements RegistryHandler {

    private final ServiceRegistry serviceRegistry;
    // Converter between json and object
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(RegistryHandlerJDK.class.getName());

    public RegistryHandlerJDK(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info(exchange.getRequestMethod() + " " + exchange.getRequestURI());
        String path = exchange.getRequestURI().getPath();

        InputStream requestBody = exchange.getRequestBody();
        RegistryRequest req = objectMapper.readValue(requestBody, RegistryRequest.class);

        if (path.equals("/register")) {
            logger.info("Register request: " + req.serviceName+", from "+new InetSocketAddress(req.host, req.port));
            serviceRegistry.register(req.serviceName, new InetSocketAddress(req.host, req.port));


            // Response
            RegistryResponse response = new RegistryResponse();
            response.success = true;
            // Respond
            respond(exchange, response);

        } else if (path.equals("/lookup")) {
            logger.info("Lookup service: " + req.serviceName);

            InetSocketAddress inetSocketAddress = serviceRegistry.lookup(req.serviceName);

            logger.info("Found service: " + req.serviceName + ", server: "+inetSocketAddress);


            // Response
            RegistryResponse response = new RegistryResponse();
            response.success = true;
            response.host = inetSocketAddress.getHostName();
            response.port = inetSocketAddress.getPort();
            // Respond
            respond(exchange, response);
        }
    }


    @Override
    public void respond(HttpExchange exchange, RegistryResponse response) throws IOException {
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

