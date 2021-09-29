package org.softauto.grpc.listener;


import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.Protocol;
import org.softauto.core.*;
import org.softauto.grpc.SoftautoGrpcUtils;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;


/** Component that sets up a gRPC client for Avro's  Serialization.
 * base on avro grpc client */
public abstract class ListenerGrpcClient {

  private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerGrpcClient.class);

  private ListenerGrpcClient() {
  }

  /**
   * Creates a gRPC client for Avro's interface with default {@link CallOptions}.
   *
   * @param iface   Avro interface for which client is built.
   * @param         <?> type of Avro Interface.
   * @return a new client proxy.
   */
  public static ServiceInvocationHandler create( Class<?> iface) {
    return create(iface, CallOptions.DEFAULT);
  }

  /**
   * Creates a gRPC client for Avro's interface with provided {@link CallOptions}.
   *
   * @param iface       Avro interface for which client is built.
   * @param callOptions client call options for gRPC.
   * @param             <?> type of Avro Interface.
   * @return a new client proxy.
   */
  public static ServiceInvocationHandler create( Class<?> iface, CallOptions callOptions) {
    Protocol protocol = SoftautoGrpcUtils.getProtocol(iface);
    AbstractServiceDescriptor serviceDescriptor = ServiceDescriptor.create(iface);
    ServiceInvocationHandler proxyHandler = new ServiceInvocationHandler( callOptions, protocol,
        serviceDescriptor,iface);

    return proxyHandler;
  }




  public static class ServiceInvocationHandler implements InvocationHandler {
    private ManagedChannel channel;
    private  CallOptions callOptions;
    private AbstractServiceDescriptor serviceDescriptor;
    Class<?> iface ;

    ServiceInvocationHandler( CallOptions callOptions, Protocol protocol,
        ServiceDescriptor serviceDescriptor) {

      this.callOptions = callOptions;
      this.serviceDescriptor = serviceDescriptor;
    }

    ServiceInvocationHandler( CallOptions callOptions, Protocol protocol,
                             AbstractServiceDescriptor serviceDescriptor, Class<?> iface) {

      this.callOptions = callOptions;
      this.serviceDescriptor = serviceDescriptor;
      this.iface = iface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  {
      try {
        return invokeUnaryMethod(method, args);
      } catch (Throwable e) {
         logger.error("invoke method fail "+ method.getName() + " args : " +Arrays.toString(args),e);
      }
      return null;
    }


    public void setIface(Class<?> iface) {
      this.iface = iface;
    }

    private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
      Type[] parameterTypes = method.getParameterTypes();
      if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
          && org.softauto.serializer.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
        // get the callback argument from the end
        Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
        Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
        org.softauto.serializer.CallFuture<?> callback = (org.softauto.serializer.CallFuture<?>) args[args.length - 1];
        unaryRequest(method.getName(), callback,finalArgs,finalTypes);
        return null;
      } else {
        return unaryRequest(method.getName(), args,parameterTypes);
      }
    }

    private Object unaryRequest(String methodName, Object...args) throws Exception {
      org.softauto.serializer.CallFuture<Object> callFuture = new org.softauto.serializer.CallFuture<>();
      unaryRequest(methodName, callFuture,args);
      try {
        return callFuture.get();
      } catch (Exception e) {
        if (e.getCause() instanceof Exception) {
          throw (Exception) e.getCause();
        }
        throw new AvroRemoteException(e.getCause());
      }
    }

   /*
    private <RespT> void unaryRequest(String methodName, Object[] args, org.softauto.serializer.CallFuture<RespT> callback)  {
      try {
        AbstractServiceDescriptor serviceDescriptor = ServiceDescriptor.create(iface);
        MethodDescriptor<Object[], Object> m = serviceDescriptor.getMethod(methodName, MethodDescriptor.MethodType.UNARY);
        channel = ManagedChannelBuilder.forAddress(Configuration.get(Context.TEST_MACHINE).asText(), Configuration.get(Context.LISTENER_PORT).asInt()).usePlaintext().build();
        StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callback, channel);
        ClientCalls.asyncUnaryCall(channel.newCall(m, callOptions), args, observerAdpater);
      }catch (Exception e){
        logger.error("unaryRequest fail ",e.getCause());

      }
    }
*/

    private <RespT> void unaryRequest(String methodName,  org.softauto.serializer.CallFuture<RespT> callback,Object...args)  {
    //public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object...args){
      try {
        logger.debug("exec rpc call "+ methodName);
        Serializer serializer;
        if(channel != null) {
          serializer = new Serializer().setChannel(channel);
        }else {
          serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asText()).setPort(Configuration.get(Context.LISTENER_PORT).asInt()).buildChannel();
        }
        Message message = Message.newBuilder().setDescriptor(methodName).setArgs((Object[]) args[0]).setTypes((Class[]) args[1]).build();
        serializer.write(message,callback);
      }catch (Exception e){
        logger.error("fail exec rpc call "+ methodName, e);
      }
    }

  }
}
