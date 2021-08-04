package org.softauto.core;

import io.grpc.MethodDescriptor;
import org.apache.avro.Protocol;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractServiceDescriptor {

    protected static final ConcurrentMap<String, AbstractServiceDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
    protected  String serviceName;
    protected Protocol protocol;
    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(AbstractServiceDescriptor.class);

    public Protocol getProtocol() {
        return protocol;
    }

    // cache for method descriptors.
    protected final ConcurrentMap<String, MethodDescriptor<Object[], Object>> methods = new ConcurrentHashMap<>();

    protected AbstractServiceDescriptor(Class iface, String serviceName) {
        try {
            this.serviceName = serviceName;
            this.protocol = Utils.getProtocol(iface);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static AbstractServiceDescriptor create(Class iface) throws Exception{
        throw new Exception("not impl");
    }

    public String getServiceName() {
        return serviceName;
    }

    public abstract MethodDescriptor<Object[], Object> getMethod(String methodName, MethodDescriptor.MethodType methodType);




}
