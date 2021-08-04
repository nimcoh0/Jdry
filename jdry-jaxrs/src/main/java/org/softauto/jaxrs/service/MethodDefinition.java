package org.softauto.jaxrs.service;

public class MethodDefinition {

    private  MethodDescriptor method;
    private CallerHandler handler;
    private ChannelDescriptor channel;

    private MethodDefinition(MethodDescriptor method, CallerHandler callHandler,ChannelDescriptor channel) {
        this.method = method;
        handler = callHandler;
        this.channel = channel;
    }

    public static  MethodDefinition create(MethodDescriptor method, CallerHandler handler,ChannelDescriptor channel) {
        return new MethodDefinition(method, handler,channel);
    }

    public MethodDescriptor getMethodDescriptor() {
        return this.method;
    }

    public CallerHandler getCallerHandler() {
        return handler;
    }

    public ChannelDescriptor getChannel() {
        return channel;
    }
}
