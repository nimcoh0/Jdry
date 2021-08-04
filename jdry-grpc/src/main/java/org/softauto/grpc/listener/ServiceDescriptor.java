package org.softauto.grpc.listener;

import io.grpc.MethodDescriptor;
import org.apache.avro.Protocol;
import org.softauto.core.AbstractServiceDescriptor;
import org.softauto.grpc.SoftautoGrpcUtils;
import org.softauto.grpc.SoftautoRequestMarshaller;
import org.softauto.grpc.SoftautoResponseMarshaller;

import static io.grpc.MethodDescriptor.generateFullMethodName;


/** Descriptor for a gRPC service based on a Avro interface. */
public class ServiceDescriptor extends AbstractServiceDescriptor {

  private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ServiceDescriptor.class);
  protected Protocol protocol;

  private ServiceDescriptor(Class iface, String serviceName) {
    super(iface,serviceName);
    protocol = SoftautoGrpcUtils.getProtocol(iface);
  }

  public static AbstractServiceDescriptor create(Class iface) {
    String serviceName = SoftautoGrpcUtils.getServiceName(iface);
    return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new ServiceDescriptor(iface,serviceName));
  }



  /**
   * Provides a gRPC {@link MethodDescriptor} for a RPC method/message of Avro
   * {@link Protocol}.
   *
   * @param methodType gRPC type for the method.
   * @return a {@link MethodDescriptor}
   */
  @Override
  public MethodDescriptor<Object[], Object> getMethod(String methodName, MethodDescriptor.MethodType methodType) {
    return methods.computeIfAbsent(methodName,
            key -> MethodDescriptor.<Object[], Object>newBuilder()
                    .setFullMethodName(generateFullMethodName(serviceName, methodName)).setType(methodType)
                    .setRequestMarshaller(new SoftautoRequestMarshaller(protocol.getMessages().get(methodName)))
                    .setResponseMarshaller(new SoftautoResponseMarshaller(protocol.getMessages().get(methodName))).build());
  }
}
