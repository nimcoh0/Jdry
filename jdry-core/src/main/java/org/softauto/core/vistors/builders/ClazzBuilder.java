package org.softauto.core.vistors.builders;

import org.softauto.core.ClassType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * schema class details builder
 */
public class ClazzBuilder {

    public static Builder newBuilder() { return new Builder();}

    public static class Builder {

        //private List<HashMap<String,Object>> constructorRequest;
        private String fullClassName;
        private ClassType initialize;

        //public Builder(){
           // constructorRequest = new ArrayList<>();
        //}


        //public List<HashMap<String, Object>> getConstructorRequest() {
           // return constructorRequest;
       // }

       // public void setConstructorRequest(List<HashMap<String, Object>> constructorRequest) {
        //    if(constructorRequest.size()> 0) {
          //      this.constructorRequest = constructorRequest;
          //  }
       // }

        public String getFullClassName() {
            return fullClassName;
        }

        public void setFullClassName(String fullClassName) {
            this.fullClassName = fullClassName;
        }

        public ClassType getInitialize() {
            return initialize;
        }

        public void setInitialize(ClassType initialize) {
            this.initialize = initialize;
        }

        public HashMap<String,Object> build(){
            HashMap<String,Object> clazz = new HashMap<>();
            //clazz.put("constructorRequest", constructorRequest);
            clazz.put("fullClassName", fullClassName);
            clazz.put("initialize", initialize);
            return clazz;
        }
    }

}
