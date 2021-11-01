package org.softauto.jaxrs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;
import java.util.*;

/**
 * build jaxrs methdo Descriptor
 */
public class MethodDescriptor {


    String produces;
    String path;
    Entity<?> entity;
    String response;
    Method method;
    String fullMethodName;
    Protocol.Message message;
    String[] content;


    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MethodDescriptor.class);


    public String getFullMethodName() {
        return fullMethodName;
    }

    public MethodDescriptor setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
        return this;
    }


    public Method getMethod() {
        return method;
    }

    public MethodDescriptor setMethod(Method method) {
        this.method = method;
        return this;
    }



    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public Protocol.Message getMessage() {
        return message;
    }

    public void setMessage(Protocol.Message message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getResponse() {
        return response;
    }

    public MethodDescriptor setResponse(String response) {
        this.response = response;
        return this;
    }

    public MethodDescriptor setPath(String path) {
        this.path = path;
        return this;
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public MethodDescriptor setEntity(Entity<?> entity) {
        this.entity = entity;
        return this;
    }

    public static  Builder newBuilder() {
        return new Builder();
    }

    public String getProduces() {
        return produces;
    }

    public MethodDescriptor setProduces(String produces) {
        this.produces = produces;
        return this;
    }

    public static String extractFullMethodName(String fullMethodName) {
        return  fullMethodName.replace("_",".");
    }


    public MethodDescriptor(String path,String produces,String response,Method method,String fullMethodName,Protocol.Message message){
        this.produces = produces;
        this.path = path;
        this.response = response;
        this.method = method;
        this.fullMethodName = fullMethodName;
        this.message  = message;
    }

    public MethodDescriptor(String path,String produces,String response,Method method,String fullMethodName,Protocol.Message message,String[] content){
        this.produces = produces;
        this.path = path;
        this.response = response;
        this.method = method;
        this.fullMethodName = fullMethodName;
        this.message  = message;
        this.content = content;

    }

    public Entity buildEntity(Protocol.Message message, Object[] args){
        Entity<?> entity = null;
        try{
            if(((HashMap)message.getObjectProp("jaxrs")).get("Consumes") != null) {
                String consumes = ((HashMap) message.getObjectProp("jaxrs")).get("Consumes").toString();
                if (consumes.equals(MediaType.APPLICATION_JSON)) {
                    Object body = getArgValue(args, content[0], message);
                    String json = new ObjectMapper().writeValueAsString(body);
                    entity = Entity.entity(json, MediaType.APPLICATION_JSON);
                }
                if (consumes.equals(MediaType.MULTIPART_FORM_DATA_TYPE)) {
                    Form form = new Form();
                    for (int i = 0; i < content.length; i++) {
                        form.param(content[i], args[i].toString());
                    }

                    entity = Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE);
                }
                if (consumes.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
                    Form form = new Form();
                    for (int i = 0; i < content.length; i++) {
                        form.param(content[i], args[i].toString());
                    }
                    entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return entity;
    }

    protected Object getArgValue(Object[] args,String content,Protocol.Message message){
        try{
            List<Schema.Field> fields = message.getRequest().getFields();
            for(int i=0;i<fields.size();i++){
                if(fields.get(i).name().equals(content)){
                    return args[i];
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static final class Builder {
        String produces;
        String path;
        Protocol.Message message;
        String[] content;
        ObjectMapper objectMapper ;
        String response;
        Method method;
        Object[] args;
        String fullMethodName;
        MethodType type;
        Class[] types;

        public Class[] getTypes() {
            return types;
        }

        public Builder setTypes(Class[] types) {
            this.types = types;
            return this;
        }

        public MethodType getType() {
            return type;
        }

        public Builder setType(MethodType type) {
            this.type = type;
            return this;
        }

        public String getFullMethodName() {
            return fullMethodName;
        }

        public Builder setFullMethodName(String fullMethodName) {
            this.fullMethodName = fullMethodName;
            return this;
        }

        public Object[] getArgs() {
            return args;
        }

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        private Builder() {
            objectMapper =  new ObjectMapper();
        }

        public void messageParser(){
            try {
                Map<String,Object> msg = message.getObjectProps();
                Map<String,Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                path = jaxrs.containsKey("Path") ? jaxrs.get("Path").toString() : null;
                produces = jaxrs.containsKey("Produces") ?  jaxrs.get("Produces").toString() : null;
                content = jaxrs.containsKey("content") ? Arrays.copyOf(((ArrayList<String>)jaxrs.get("content")).toArray(), ((ArrayList<String>)jaxrs.get("content")).toArray().length, String[].class)  : null;
                response = jaxrs.containsKey("response") ?  jaxrs.get("response").toString(): null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public Method getMethod() {
            return method;
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }


        public String getProduces() {
            return produces;
        }

        public Builder setProduces(String produces) {
            this.produces = produces;
            return this;
        }

        public String getPath() {
            return path;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Protocol.Message getMessage() {
            return message;
        }

        public Builder setMessage(Protocol.Message message) {
            this.message = message;
            return this;
        }

        public String[] getContent() {
            return content;
        }

        public Builder setContent(String[] content) {
            this.content = content;
            return this;
        }





        public MethodDescriptor build() {
            messageParser();
            if(content != null){
                return new MethodDescriptor(path,produces,response,method,fullMethodName,message,content);
            }
            return new MethodDescriptor(path,produces,response,method,fullMethodName,message);

        }


    }



    public static enum MethodType {

        GET,
        PUT,
        POST,
        DELETE,
        NONE;

        private MethodType() {
        }



        public static MethodType fromString(String text) {
            for (MethodType b : MethodType.values()) {
                if (b.name().equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

}
