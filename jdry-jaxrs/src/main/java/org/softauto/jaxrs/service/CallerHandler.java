package org.softauto.jaxrs.service;


import org.softauto.core.CallOptions;

import java.util.Map;

public interface CallerHandler {
    <T> T startCall(MethodDescriptor methodDescriptor, Object[] args, Map<String, Object> msg, Class<T> res);
}
