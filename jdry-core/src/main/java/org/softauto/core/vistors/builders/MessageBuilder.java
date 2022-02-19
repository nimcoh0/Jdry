package org.softauto.core.vistors.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.softauto.core.Utils;

import java.util.ArrayList;
import java.util.Arrays;
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
        String description;
        String annotations;
        String args;
        String result;

        public String getArgs() {
            return args;
        }

        public Builder setArgs(String args) throws Exception{
             this.args = args;
             return this;
        }

        public String getResult() {
            return result;
        }

        public Builder setResult(String result) throws Exception{
            this.result = result;
            return this;
        }

        public Builder(){
            request = new ArrayList<>();
            clazz = new HashMap<>();
        }

        public String getAnnotations() {
            return annotations;
        }

        public Builder setAnnotations(List<String> annotations) {
            try {
                this.annotations = Arrays.toString(annotations.toArray());
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
            message.put("description",description);
            message.put("args",args);
            message.put("result",result);
            message.put("annotations",annotations);
            return message;
        }
    }

}
