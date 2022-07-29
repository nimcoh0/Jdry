package org.softauto.scanner.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.softauto.serializer.*;



import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * schema method detail builder
 */
public class MessageBuilder {

    public static Builder newBuilder() { return new Builder();}


    public static class Builder {

        //String filter = "ExposedForTesting";

        List<HashMap<String,Object>> request;
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> clazz;
        String namespace;
        String method;
        //String type;
        String transceiver;
        String description;
        HashMap<String,Object> annotations;
        //Object[] args;
        Object result;
        //String domain;
        //String kind;
        long timestamp;
        //long threadId;
        //RefType refType;
       // String element;
        List<HashMap<String,Object>> events;
        ObjectMapper objectMapper;
        int id = -1;
        TypeMapper typeMapper = new TypeMapper();
        //int orderId ;
        //String caller;


        public int getId() {
            return id;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }


       // public Builder setOrderId(int orderId) {
           // this.orderId = orderId;
           // return this;
        //}

        public List<HashMap<String, Object>> getEvents() {
            return events;
        }



        public Builder setEvents(List<HashMap<String, Object>> events) {
            this.events = events;
            return this;
        }



        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }








        public Object getResult() {
            return result;
        }

        public Builder setResult(Object result) throws Exception{
            this.result = result;
            return this;
        }

        public Builder(){
            request = new ArrayList<>();
            clazz = new HashMap<>();
            objectMapper = new ObjectMapper();
            SimpleModule userModule = new SimpleModule();
            userModule.addSerializer(Response.class, new WsRsResponsSerializer());
            userModule.addSerializer(ContainerRequest.class, new ContainerRequestSerializer());
            userModule.addSerializer(ContainerResponse.class, new ContainerResponseSerializer());
            //userModule.addSerializer(Object.class,new DepthJsonSerializer());
            //objectMapper.registerModule(userModule);
           // ExtBeanSerializerModifier extBeanSerializerModifier = new ExtBeanSerializerModifier();
           // extBeanSerializerModifier.setMaxDepth(10);
           // extBeanSerializerModifier.setDepthLimitEnabled(true);
           // extBeanSerializerModifier.setLoopPreventionEnabled(false);
           // extBeanSerializerModifier.setSerializeBigObjects(true);
           // userModule.setSerializerModifier(extBeanSerializerModifier);

            objectMapper.registerModule(userModule);
            typeMapper.register(new javax_ws_rs_core_response());
            typeMapper.register(new org_glassfish_jersey_server_containerResponse());
            typeMapper.register(new org_glassfish_jersey_server_containerRequest());
            typeMapper.register(new java_lang_null());
            //objectMapper.enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);
           // objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            //objectMapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
            //objectMapper.enable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
            //objectMapper.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);

        }




        public HashMap<String,Object> getAnnotations() {
            return annotations;
        }

        public Builder setAnnotations(HashMap<String,Object> annotations) {
            try {
                this.annotations = annotations;
            }catch (Exception e){
                e.printStackTrace();
            }
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getTransceiver() {
            return transceiver;
        }

        public Builder setTransceiver(String transceiver) {
            this.transceiver = transceiver;
            return this;
        }

        public String getNamespace() {
            return namespace;
        }



        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public String getMethod() {
            return method;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public HashMap<String, Object> getClazz() {
            return clazz;
        }

        public Builder setClazz(HashMap<String, Object> clazz) {
            this.clazz = clazz;
            return this;
        }


        public List<HashMap<String, Object>> getRequest() {
            return request;
        }

        public Builder setRequest(List<HashMap<String, Object>> request) {
            this.request = request;
            return this;
        }



        public <T> Builder  addRequest(String key, Class<T> type, Object value){
            HashMap<String,Object> req = new HashMap<>();
            ObjectNode nodeType = new ObjectMapper().createObjectNode();
            //ObjectNode nodeName = new ObjectMapper().createObjectNode();
            try {
                //nodeName.put("name",key);
                if(type.getTypeName().contains(".")) {
                    nodeType.put("name", type.getTypeName().substring(type.getTypeName().lastIndexOf(".") + 1));
                    nodeType.put("namespace", type.getTypeName().substring(0, type.getTypeName().lastIndexOf(".")));
                }else {
                    nodeType.put("name", type.getTypeName());
                }
                nodeType.put("full",type.getTypeName());
                if (Utils.isSchemaType(type.getTypeName())){
                    if(type.getTypeName().contains(".")) {
                        nodeType.put("type", type.getTypeName().substring(type.getTypeName().lastIndexOf(".") + 1));
                    }else {
                        nodeType.put("type", type.getTypeName());
                    }

                }else {
                   nodeType.put("type","external");

               }
                req.put("type", (JsonNode)nodeType);
                req.put("name", key);
                req.put("current",  value);

               request.add(req);
            }catch (Exception e){
                e.printStackTrace();
            }
            return this;
        }


        public HashMap<String,Object> getResponse() {
            return response;
        }

        public Builder setResponse(String type,Object value) {

            try {
                String r = objectMapper.writeValueAsString(value);
                String t = typeMapper.getTypeAsString(value);
                response.put("current", r);
                if (Utils.isSchemaType(t)){
                    if(t.contains(".")) {
                        response.put("type", t.substring(t.lastIndexOf(".") + 1));
                    }else {
                        response.put("type", t);
                    }
                }else {
                    response.put("type", t);
                }

            }catch (Exception e){
                e.printStackTrace();
                response.put("current", null);
            }
            return this;
        }






        public HashMap<String,Object> build() {
            HashMap<String,Object> message = new HashMap<>();
            try {

                message.put("request", request);
                message.put("response", response);
                message.put("class", clazz);
                message.put("namespace", namespace);
                message.put("method", method);
                message.put("transceiver", transceiver);
                //message.put("type", type);
                message.put("description", description);
                //message.put("args", buildArgs());
                //message.put("result",buildResult());
                message.put("id", id);
                message.put("annotations", annotations);
                message.put("events", events);
                //message.put("kind",kind);
                message.put("timestamp", timestamp);
                //message.put("threadId", threadId);
                //message.put("caller", caller);
                //message.put("orderId", orderId);
                //message.put("refType",refType.name());
                //message.put("element", element);
            }catch (Exception e){
                e.printStackTrace();
            }
            return message;
        }
    }

}
