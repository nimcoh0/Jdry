package org.softauto.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * generic class that hold the configuration
 * support json query
 */
public class Configuration {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Configuration.class);
    static JsonNode configuration = new ObjectMapper().createObjectNode();


    public static void setConfiguration(JsonNode configuration){
        try {
            Configuration.configuration = configuration;
        }catch (Exception e){
            logger.error("fail update configuration ", e);
        }
    }

    public static JsonNode getConfiguration() {
        return configuration;
    }

    public static JsonNode get(String path){
        if(configuration.at("/"+path) != null && !configuration.at("/"+path).asText().isEmpty()){
                return  configuration.at("/"+path);
         }

        return null;
    }

    public static void put(String key,String value){
        ((ObjectNode)configuration).put(key,value);
    }

    public static void put(String key,Boolean value){
        ((ObjectNode)configuration).put(key,value);
    }
}
