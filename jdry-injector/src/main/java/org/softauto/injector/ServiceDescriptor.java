package org.softauto.injector;


import org.apache.avro.Protocol;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/** Descriptor for a gRPC service based on a Avro interface. */
class ServiceDescriptor {

  // cache for service descriptors.
  private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServiceDescriptor.class);
  private static  ConcurrentMap<String, ServiceDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
  private  String serviceName;
  private Protocol protocol;
  Class iface ;


  // cache for method descriptors.
  private final ConcurrentMap<String, ClassDescriptor> classes = new ConcurrentHashMap<>();

  public ConcurrentMap<String, ClassDescriptor> getClasses() {
    return classes;
  }

  private ServiceDescriptor(Class iface, String serviceName) {
    this.serviceName = serviceName;
    this.protocol = SoftautoGrpcUtils.getProtocol(iface);
    this.iface = iface;
  }

  public ServiceDescriptor(String serviceName, List<ClassDescriptor> classDescriptors){
    this.serviceName = serviceName;

  }

  /**
   * Creates a Service Descriptor.
   *
   * @param iface Avro RPC interface.
   */
  public static ServiceDescriptor create(Class iface) {
    String serviceName = SoftautoGrpcUtils.getServiceName(iface);
    return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new ServiceDescriptor(iface, serviceName));
  }

  /**
   * provides name of the service.
   */
  public String getServiceName() {
    return serviceName;
  }

  /**
   * Provides a gRPC {@link ClassDescriptor} for a RPC method/message of Avro
   * {@link Protocol}.
   *
   * @param classType gRPC type for the method.
   * @return a {@link ClassDescriptor}
   */
  public ClassDescriptor getClasses(ClassType classType,Protocol.Message msg) {
    String className = ((HashMap)msg.getObjectProp("class")).get("fullClassName").toString();
    return classes.computeIfAbsent(className,
        key -> ClassDescriptor.<Object[], Object>newBuilder()
            .setFullClassName(className)
            .setType(classType)
            .setMsg(msg)
            .build());

  }


  public ClassDescriptor getClasses(Protocol.Message msg, ClassType classType) {
    String className = ((HashMap)msg.getObjectProp("class")).get("fullClassName").toString();
    return classes.computeIfAbsent(className,
            key -> ClassDescriptor.<Object[], Object>newBuilder()
                    .setFullClassName(className)
                    .setType(classType)
                    .setTypes(Utils.extractConstructorDefaultArgsTypes(className))
                    .setArgs(Utils.getConstructorDefaultValues(className))
                    .setMsg(msg)
                    .build0());

  }


}
