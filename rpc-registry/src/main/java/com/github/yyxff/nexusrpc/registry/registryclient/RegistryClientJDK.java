package com.github.yyxff.nexusrpc.registry.registryclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yyxff.nexusrpc.registry.RegistryClient;
import com.github.yyxff.nexusrpc.registry.messagestruct.RegistryRequest;
import com.github.yyxff.nexusrpc.registry.messagestruct.RegistryResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class RegistryClientJDK implements RegistryClient {

    // Url to look up server list of a service
    private final String lookUpUrl = "http://localhost:8888/lookup";
    private final String registryUrl = "http://localhost:8888/register";
    // Logger
    private static final Logger logger = Logger.getLogger(RegistryClientJDK.class.getName());


    @Override
    public void register(String interfaceName, InetSocketAddress address) {
        try{
            // Construct request body
            RegistryRequest req = new RegistryRequest();
            req.host = address.getHostName();
            req.port = address.getPort();
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
            return;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InetSocketAddress> lookup(String interfaceName) {
        try {
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
            return resp.serverList;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
