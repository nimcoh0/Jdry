package org.softauto.logger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * helper class for logs build
 */
public class LogBuilder {

    public static Builder newBuilder() { return new Builder();}
    private Class<String>[] classes;
    private Object[] arguments;

    public LogBuilder(Class<String>[] classes, Object[] arguments){
        this.arguments = arguments;
        this.classes = classes;
    }

    public String toJson(){
        try {
          return  new ObjectMapper().writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public Class<String>[] getClasses() {
        return classes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public static class Builder {
        private Class<String>[] classes;
        private Object[] arguments;

        public Class[] getClasses() {
            return classes;
        }

        public Builder setClasses(Class[] classes) {
            this.classes = classes;
            return this;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public Builder setArguments(Object...arguments) {
            this.arguments = arguments;
            return this;
        }

        public LogBuilder build(){
            classes = new Class[arguments.length];
            for(int i=0;i<arguments.length;i++){
                classes[i] = String.class;
            }
            return new LogBuilder(classes,arguments);
        }
    }

}
