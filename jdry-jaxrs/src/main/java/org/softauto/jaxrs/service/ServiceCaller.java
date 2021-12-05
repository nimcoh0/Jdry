package org.softauto.jaxrs.service;

import java.util.Map;

public class ServiceCaller {

    public static CallerHandler call(UnaryClass method) {
        return new Call(method);
    }

    public interface requestMethod {
        <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,Map<String, Object> msg,Class<T> res);
    }

    public interface UnaryClass extends requestMethod {
    }

    public static  class Call implements CallerHandler {
        private final requestMethod method;


        Call(requestMethod method) {
            this.method = method;

        }
        @Override
        public <T> T startCall(MethodDescriptor methodDescriptor, Object[] args, Map<String, Object> msg,Class<T> res) {
            return  (T)this.method.invoke(methodDescriptor,args,msg,res);
        }


    }



}
