package com.github.yyxff.nexusrpc.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

public class ConfigLoader {
    public static RpcConfig load(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(path), RpcConfig.class);
    }
}
