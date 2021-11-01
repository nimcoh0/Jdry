package org.softauto.discovery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.softauto.discovery.schema.SchemaHeaderHandler;
import org.softauto.discovery.schema.SchemaMessageHandler;
import org.softauto.discovery.schema.SchemaTypesHandler;


public class SchemaVisitor implements Visitor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SchemaVisitor.class);
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode schema;
    JsonNode node ;
    String protocol;

    public SchemaVisitor(String protocol,JsonNode node,JsonNode schema){
        this.node = node;
        this.protocol = protocol;
        this.schema = (ObjectNode)schema;
    }

    @Override
    public void visit(SchemaHeaderHandler p) {
        schema.put("protocol",protocol);
        schema.put("version",p.getVersion());
        schema.put("namespace",p.getNamespace());
        if(schema.get("types") == null) {
            schema.put("types", objectMapper.createArrayNode());
        }
        if(schema.get("messages") == null) {
            schema.put("messages", objectMapper.createObjectNode());
        }
    }

    @Override
    public void visit(SchemaTypesHandler p) {
        if(node != null){
            JsonNode types = node.get("types");
            if(types != null) {
                p.marge(schema.get("types"), types);
                JsonNode n = p.removeDuplicates(schema.get("types"));
                schema.set("types",n);
            }
        }
    }

    @Override
    public void visit(SchemaMessageHandler p) {
        if(node != null){
            JsonNode message = node.get("messages");
            if(message != null)
                ((ObjectNode)schema.get("messages")).setAll((ObjectNode) message);
        }
    }

}
