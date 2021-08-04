package org.softauto.jaxrs.service;


public interface CallerHandler {
    <T> T startCall(MethodDescriptor methodDescriptor,Object[] args,ChannelDescriptor channel,Class<T> res);
}
