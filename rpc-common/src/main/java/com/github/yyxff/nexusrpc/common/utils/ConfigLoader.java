package com.github.yyxff.nexusrpc.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

public class ConfigLoader {
    /*
     * Read a config as RpcConfig instance from |path|
     */
    public static RpcConfig load(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(path), RpcConfig.class);
    }
}
