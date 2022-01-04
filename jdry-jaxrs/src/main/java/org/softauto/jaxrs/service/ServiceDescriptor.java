package org.softauto.jaxrs.service;


import org.apache.avro.Protocol;
import org.softauto.core.Utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServiceDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServiceDescriptor.class);
    // cache for service descriptors.
    private static final ConcurrentMap<String, ServiceDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
    private  String serviceName;
    private  Protocol protocol;
    Class iface ;


    // cache for method descriptors.
    private final ConcurrentMap<String, MethodDescriptor> methods = new ConcurrentHashMap<>();

    public ConcurrentMap<String, MethodDescriptor> getMethods() {
        return methods;
    }

    private ServiceDescriptor(Class iface, String serviceName) {
        this.serviceName = serviceName;
        this.protocol = Utils.getProtocol(iface);
        this.iface = iface;
    }

    private ServiceDescriptor(Protocol protocol, String serviceName) {
        this.serviceName = serviceName;
        this.protocol = protocol;

    }

    public ServiceDescriptor(String serviceName, List<MethodDescriptor> methodDescriptors){
        this.serviceName = serviceName;

    }

    /**
     * Creates a Service Descriptor.
     *
     * @param iface Avro RPC interface.
     */
    public static ServiceDescriptor create(Class iface) {
        String serviceName = Utils.getServiceName(iface);
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new ServiceDescriptor(iface, serviceName));
    }

    public static ServiceDescriptor create(Protocol protocol) {
        String serviceName = Utils.getServiceName(protocol);
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new ServiceDescriptor(protocol, serviceName));
    }

    /**
     * provides name of the service.
     */
    public String getServiceName() {
        return serviceName;
    }




    public MethodDescriptor getMethods(Method method, Protocol.Message msg, MethodDescriptor.MethodType methodType) {
        return methods.computeIfAbsent(MethodDescriptor.extractFullMethodName(method.getName()),
                key -> MethodDescriptor.<Object[], Object>newBuilder()
                        .setFullMethodName(MethodDescriptor.extractFullMethodName(method.getName()))
                        .setType(methodType)
                        .setTypes(method.getParameterTypes())
                        .setMessage(msg)
                        .setMethod(method)
                        .build());

    }

}
