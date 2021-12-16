package org.softauto.injector;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServiceDefinition {

    ServiceDescriptor serviceDescriptor;
    Map<String, ClassDefinition> classes;



    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServiceDefinition.class);
    public static Builder builder(ServiceDescriptor serviceDescriptor) {
        return new Builder(serviceDescriptor);

    }



    private ServiceDefinition(ServiceDescriptor serviceDescriptor, Map<String, ClassDefinition> classes) {
        this.serviceDescriptor = (ServiceDescriptor)Preconditions.checkNotNull(serviceDescriptor, "serviceDescriptor");
        this.classes = Collections.unmodifiableMap(new HashMap(classes));
    }


    public Collection<ClassDefinition> getClasses() {
        return this.classes.values();
    }


    public boolean isExist(String className){
        if(classes != null && classes.containsKey(className)){
            return true;
        }
        return false;
    }

    public ClassDefinition getClazz(String className) {
        return (ClassDefinition)this.classes.get(className);
    }



    public static final class Builder {
        private  ServiceDescriptor serviceDescriptor;
        private  Map<String, ClassDefinition> classes;

        private Builder(String serviceName) {
            this.classes = new HashMap();
            this.serviceDescriptor = null;
        }

        private Builder(ServiceDescriptor serviceDescriptor) {
            this.classes = new HashMap();
            this.serviceDescriptor = (ServiceDescriptor)Preconditions.checkNotNull(serviceDescriptor, "serviceDescriptor");

        }

        public Builder addClass(ClassDescriptor clazz, CallerHandler callHandler) {
            return addClass(ClassDefinition.create((ClassDescriptor)Preconditions.checkNotNull(clazz, "class must not be null"), (CallerHandler)Preconditions.checkNotNull(callHandler, "handler must not be null")));
        }

        public   Builder addClass(ClassDefinition def) {
            ClassDescriptor clazz = def.getClassDescriptor();
            String name = clazz.getFullClassName();
            if(!this.classes.containsKey(name)) {
                this.classes.put(name, def);
            }
            return this;
        }

        public ServiceDefinition build() {
            return new ServiceDefinition(serviceDescriptor, this.classes);

        }

    }
}
