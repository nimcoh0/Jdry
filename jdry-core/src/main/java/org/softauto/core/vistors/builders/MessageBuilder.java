package org.softauto.core.vistors.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * schema method detail builder
 */
public class MessageBuilder {

    public static Builder newBuilder() { return new Builder();}


    public static class Builder {

        List<HashMap<String,Object>> request;
        String response;
        HashMap<String,Object> clazz;
        String namespace;
        String method;
        String type;
        String transceiver;

        public Builder(){
            request = new ArrayList<>();
            clazz = new HashMap<>();
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

        public String getType() {
            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
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

        public Builder addRequest(String key,Object type,Object value){
            HashMap<String,Object> req = new HashMap<>();
            req.put("name",key);
            req.put("type",type);
            if(value != null){
                if(((HashMap)value).size()> 1){
                    req.put("default", value);
                }else {
                    req.put("default", ((HashMap) value).get("value"));
                }
            }
            request.add(req);
            return this;
        }


        public String getResponse() {
            return response;
        }

        public Builder setResponse(String response) {
            this.response = response;
            return this;
        }

        public HashMap<String,Object> build(){
            HashMap<String,Object> message = new HashMap<>();
            message.put("request",request);
            message.put("response",response);
            message.put("class",clazz);
            message.put("namespace",namespace);
            message.put("method",method);
            message.put("transceiver", transceiver);
            message.put("type", type);
            return message;
        }
    }

}
