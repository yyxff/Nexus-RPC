package com.github.yyxff.nexusrpc.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yyxff.nexusrpc.registry.RegistryRequest;
import com.github.yyxff.nexusrpc.registry.RegistryResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class RegisterHandler {

    private final String registryUrl = "http://localhost:8888/register";
    private static final Logger logger = Logger.getLogger(RegisterHandler.class.getName());


    public RegistryResponse registry(String interfaceName) throws IOException {

        // Construct request body
        RegistryRequest req = new RegistryRequest();
        req.host = "127.0.0.1";
        req.port = 8080;
        req.serviceName = interfaceName;
        // logger.info("register url: " + registryUrl);

        // Request
        URL url = new URL(registryUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        // logger.info("ready to request");

        // Write
        OutputStream os = conn.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(req);  // Jackson
        os.write(json.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
        // logger.info("done json write");

        // Response
        InputStream responseStream = conn.getInputStream();
        RegistryResponse resp = mapper.readValue(responseStream, RegistryResponse.class);
        return resp;
    }
}
