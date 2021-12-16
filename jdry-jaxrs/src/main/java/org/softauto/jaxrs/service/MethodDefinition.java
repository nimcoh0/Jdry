package org.softauto.jaxrs.service;

import java.util.Map;

public class MethodDefinition {

    private  MethodDescriptor method;
    private CallerHandler handler;
    private Map<String, Object> msg;

    private MethodDefinition(MethodDescriptor method, CallerHandler callHandler,Map<String, Object> msg) {
        this.method = method;
        handler = callHandler;
        this.msg = msg;
    }

    public static  MethodDefinition create(MethodDescriptor method, CallerHandler handler, Map<String, Object> msg) {
        return new MethodDefinition(method, handler,msg);
    }

    public MethodDescriptor getMethodDescriptor() {
        return this.method;
    }

    public CallerHandler getCallerHandler() {
        return handler;
    }

    public Map<String, Object> getMsg(){
        return msg;
    }

}
