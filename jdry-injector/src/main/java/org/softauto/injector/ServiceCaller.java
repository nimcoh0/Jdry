package org.softauto.injector;

public class ServiceCaller {

    public static CallerHandler call(UnaryClass clazz) {
        return new Call(clazz);
    }

    public interface requestClass {
        Object[] invoke(ClassDescriptor classDescriptor);
        Object[] invoke(ClassDescriptor classDescriptor,Object[] args);
    }

    public interface UnaryClass extends requestClass {
    }

    public static  class Call implements CallerHandler {
        private  requestClass clazz;

        Call(requestClass clazz) {
            this.clazz = clazz;
        }

        public Object[] startCall(ClassDescriptor classDescriptor) {
            return  this.clazz.invoke(classDescriptor);
        }

        @Override
        public Object[] startCall(ClassDescriptor classDescriptor, Object[] args) {
            return  this.clazz.invoke(classDescriptor,args);
        }
    }



}
