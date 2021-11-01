package org.softauto.discovery.schema;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.softauto.discovery.AbstractSchema;
import org.softauto.discovery.Visitor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Handler for avro schema types
 */
public class SchemaTypesHandler extends AbstractSchema {

    ObjectMapper objectMapper = new ObjectMapper();
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SchemaTypesHandler.class);

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public JsonNode marge(JsonNode orgNode,JsonNode newNode){
        try {
            newNode.forEach((k)->{

                if(orgNode.isArray()){
                    ((ArrayNode) orgNode).add(k);
                }else {
                    ((ObjectNode) orgNode).setAll((ObjectNode) k);
                }
            });
        }catch (Exception e){
            logger.error("fail marge nodes ",e);
        }
        return orgNode;
    }

    public JsonNode removeDuplicates(JsonNode types){
        JsonNode node = null;
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String jsonWithDuplicates = objectMapper.writeValueAsString(types);
            List<Map<String, Object>> attributesWithDuplicates = objectMapper.readValue(jsonWithDuplicates , new TypeReference<List<Map<String, Object>>>(){});
            for(int l=0;l<attributesWithDuplicates.size();l++){
                String name = attributesWithDuplicates.get(l).get("name").toString();
                String type = attributesWithDuplicates.get(l).get("type").toString();
                Iterator<Map<String, Object>> itr = attributesWithDuplicates.subList(l+1,attributesWithDuplicates.size()).iterator();
                while (itr.hasNext()) {
                    Map<String, Object> i = itr.next();
                    String n = i.get("name").toString();
                    String t = i.get("type").toString();
                    if (name.equals(n) && type.equals(t)) {
                        itr.remove();

                    }
                }

            }
            String json = objectMapper.writeValueAsString(attributesWithDuplicates);
            node = objectMapper.readTree(json);
        } catch (Exception e) {
           logger.error("fail remove Duplicates type ",e);
        }
       return node;
    }


}
