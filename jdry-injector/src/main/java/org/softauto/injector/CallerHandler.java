package org.softauto.injector;


public interface CallerHandler {
    Object[] startCall(ClassDescriptor classDescriptor);
    Object[] startCall(ClassDescriptor classDescriptor,Object[] args);
}
