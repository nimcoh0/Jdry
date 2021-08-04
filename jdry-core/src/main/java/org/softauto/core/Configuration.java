package org.softauto.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * generic class that hold the configuration
 * support json query
 */
public class Configuration {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(Configuration.class);
    static JsonNode configuration;

    public static void setConfiguration(JsonNode configuration){
        Configuration.configuration = configuration;
    }

    public static JsonNode getConfiguration() {
        return configuration;
    }

    public static JsonNode get(String path){
        if(configuration.at("/"+path) != null){
                return  configuration.at("/"+path);
         }
        logger.warn(path + " dose not exist in configuration");
        return null;
    }

    public static void put(String key,String value){
        ((ObjectNode)configuration).put(key,value);
    }

}
