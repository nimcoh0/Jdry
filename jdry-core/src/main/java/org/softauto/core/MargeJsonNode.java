package org.softauto.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * marge two json nodes
 */
public class MargeJsonNode {

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode mergeNode(ObjectNode mainNode, ObjectNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {
            String key = fieldNames.next();
            JsonNode value = updateNode.get(key);
            if(!mainNode.has(key)) {
                mainNode.set(key, value);
            } else {
                if(value.isObject()) {
                    mergeNode((ObjectNode) mainNode.get(key), (ObjectNode) value);
                } else if(value.isArray()) {
                    ArrayNode arrayNode = (ArrayNode) value;
                    if(mainNode.get(key).isArray()){
                        arrayNode.addAll((ArrayNode) mainNode.get(key));
                    }else {
                        arrayNode.add(mainNode.get(key));
                    }
                    mainNode.set(key, arrayNode);
                } else if(value.isTextual() && (value.asText().startsWith("["))) {
                    try {
                        ArrayNode arrayNode = ((ArrayNode)objectMapper.readTree(value.asText())).addAll(
                                (ArrayNode) objectMapper.readTree(mainNode.get(key).asText()));

                        mainNode.set(key, arrayNode);
                    } catch (IOException e) {
                        //handle as string
                        mainNode.set(key, value);
                    }
                } else if(value.isTextual() && (value.asText().startsWith("{"))) {
                    try {
                        JsonNode node1 = objectMapper.readTree(value.asText());
                        JsonNode node2 = objectMapper.readTree(mainNode.get(key).asText());
                        mainNode.set(key, mergeNode((ObjectNode) node1, (ObjectNode) node2));
                    } catch (IOException e) {
                        //handle as string
                        mainNode.set(key,value);
                    }
                }else if(value.isTextual() ) {
                    mainNode.set(key,value);
                }
            }
        }
        return mainNode;
    }

    public String jsonNodeToMapString(JsonNode node) throws JsonProcessingException {
        Map<String,String> dynamicArgs = new HashMap<>();
        Iterator<String> iter = node.fieldNames();
        while(iter.hasNext()) {
            String key = iter.next();
            JsonNode valueNode = node.get(key);
            if(valueNode.isObject() || valueNode.isArray()) {
                dynamicArgs.put(key, valueNode.toString().replace("\"","\\\"") );
            } else {
                dynamicArgs.put(key, valueNode.asText());
            }
        }
        return objectMapper.writeValueAsString(dynamicArgs);
    }

}
