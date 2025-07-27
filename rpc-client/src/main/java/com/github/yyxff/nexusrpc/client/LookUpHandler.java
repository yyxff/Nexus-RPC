package com.github.yyxff.nexusrpc.client;


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

public class LookUpHandler {

    private final String lookUpUrl = "http://localhost:8888/lookup";
    private static final Logger logger = Logger.getLogger(LookUpHandler.class.getName());


    public RegistryResponse lookUp(String interfaceName) throws IOException {

        // Construct request body
        RegistryRequest req = new RegistryRequest();
        req.serviceName = interfaceName;
         logger.info("lookup service: " + interfaceName);

        // Request
        URL url = new URL(lookUpUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
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
