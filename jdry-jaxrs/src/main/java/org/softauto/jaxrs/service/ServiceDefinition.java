package org.softauto.jaxrs.service;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ServiceDefinition {
    ServiceDescriptor serviceDescriptor;
    Map<String, MethodDefinition> methods;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServiceDefinition.class);

    public static Builder builder(ServiceDescriptor serviceDescriptor) {
        return new Builder(serviceDescriptor);
    }

    private ServiceDefinition(ServiceDescriptor serviceDescriptor, Map<String, MethodDefinition> methods) {
        this.serviceDescriptor = (ServiceDescriptor) Preconditions.checkNotNull(serviceDescriptor, "serviceDescriptor");
        this.methods = Collections.unmodifiableMap(new HashMap(methods));
    }

    public Collection<MethodDefinition> getMethods() {
        return this.methods.values();
    }


    public MethodDefinition getMethod(String methodName) {
        return (MethodDefinition)this.methods.get(methodName);
    }




    public static final class Builder {
        //private final String serviceName;
        private final ServiceDescriptor serviceDescriptor;
        private final Map<String, MethodDefinition> methods;

        private Builder(String serviceName) {
            this.methods = new HashMap();
            this.serviceDescriptor = null;
        }

        private Builder(ServiceDescriptor serviceDescriptor) {
            this.methods = new HashMap();
            this.serviceDescriptor = (ServiceDescriptor)Preconditions.checkNotNull(serviceDescriptor, "serviceDescriptor");

        }

        public Builder addMethod(MethodDescriptor method, CallerHandler handler,Map<String, Object> msg) {
            return this.addMethod(MethodDefinition.create((MethodDescriptor)Preconditions.checkNotNull(method, "method must not be null"), (CallerHandler)Preconditions.checkNotNull(handler, "handler must not be null"),msg));
        }

        public   Builder addMethod(MethodDefinition def) {
            MethodDescriptor method = def.getMethodDescriptor();
            String name = method.getFullMethodName();
            Preconditions.checkState(!this.methods.containsKey(name), "Method by same name already registered: %s", name);
            this.methods.put(name, def);
            return this;
        }

        public ServiceDefinition build() {
            Map<String, MethodDefinition> tmpMethods = new HashMap(this.methods);

            ConcurrentMap<String, MethodDescriptor> entrys = serviceDescriptor.getMethods();
            Set<Map.Entry<String, MethodDescriptor>> entrySet = entrys.entrySet();
            Iterator<Map.Entry<String, MethodDescriptor>> iterator = entrySet.iterator();

            MethodDefinition removed;
            MethodDescriptor descriptorMethod;
            do {
                if (!iterator.hasNext()) {
                    if (tmpMethods.size() > 0) {
                        throw new IllegalStateException("No entry in descriptor matching bound method " + ((MethodDefinition)tmpMethods.values().iterator().next()).getMethodDescriptor().getFullMethodName());
                    }

                    return new ServiceDefinition(serviceDescriptor, this.methods);
                }

                descriptorMethod = (MethodDescriptor)iterator.next().getValue();
                removed = (MethodDefinition)tmpMethods.remove(descriptorMethod.getFullMethodName());
                if (removed == null) {
                    throw new IllegalStateException("No method bound for descriptor entry " + descriptorMethod.getFullMethodName());
                }
            } while(removed.getMethodDescriptor() == descriptorMethod);

            throw new IllegalStateException("Bound method for " + descriptorMethod.getFullMethodName() + " not same instance as method in service descriptor");
        }
    }
}
