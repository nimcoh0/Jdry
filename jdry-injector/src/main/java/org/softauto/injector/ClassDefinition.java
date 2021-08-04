package org.softauto.injector;

public class ClassDefinition {

    private  ClassDescriptor clazz;
    private CallerHandler handler;

    private ClassDefinition(ClassDescriptor clazz, CallerHandler callHandler) {
        this.clazz = clazz;
        handler = callHandler;
    }

    public static  ClassDefinition create(ClassDescriptor clazz, CallerHandler handler) {
        return new ClassDefinition(clazz, handler);
    }

    public ClassDescriptor getClassDescriptor() {
        return this.clazz;
    }

    public CallerHandler getCallHandler() {
        return handler;
    }


}
