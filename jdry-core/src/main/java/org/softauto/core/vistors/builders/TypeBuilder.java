package org.softauto.core.vistors.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * schema type detail builder
 */
public class TypeBuilder {

    public static Builder newBuilder() { return new Builder();}

    public static class Builder {


        List<HashMap<String,Object>> types;

        public Builder(){
            types = new ArrayList<>();
        }


        public List<HashMap<String, Object>> getTypes() {
            return types;
        }

        public void setTypes(List<HashMap<String, Object>> types) {
            this.types = types;
        }

        public void addType(String key,Object value){
            HashMap<String,Object> type = new HashMap<>();
            type.put("name",key);
            type.put("type",value);
            types.add(type);
        }

        public List<HashMap<String,Object>> build(){
            return types;
        }

    }
}
