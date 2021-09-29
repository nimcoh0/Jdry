/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.softauto.listener;

import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.Protocol;
import org.softauto.core.AbstractServiceDescriptor;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.core.Configuration;
import org.softauto.grpc.SoftautoGrpcUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;


/** Component that sets up a gRPC client for Avro's IDL and Serialization.
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
    private  AbstractServiceDescriptor serviceDescriptor;
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
        Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
        org.softauto.serializer.CallFuture<?> callback = (org.softauto.serializer.CallFuture<?>) args[args.length - 1];
        unaryRequest(method.getName(), finalArgs, callback);
        return null;
      } else {
        return unaryRequest(method.getName(), args);
      }
    }

    private Object unaryRequest(String methodName, Object[] args) throws Exception {
      org.softauto.serializer.CallFuture<Object> callFuture = new org.softauto.serializer.CallFuture<>();
      unaryRequest(methodName, args, callFuture);
      try {
        return callFuture.get();
      } catch (Exception e) {
        if (e.getCause() instanceof Exception) {
          throw (Exception) e.getCause();
        }
        throw new AvroRemoteException(e.getCause());
      }
    }

    /**
     * send messages to the listener server
     * @param methodName
     * @param args
     * @param callback
     * @param <RespT>
     * @throws Exception
     */
    private <RespT> void unaryRequest(String methodName, Object[] args, org.softauto.serializer.CallFuture<RespT> callback)  {
      try {
        AbstractServiceDescriptor serviceDescriptor = ServiceDescriptor.create(iface);
        MethodDescriptor<Object[], Object> m = serviceDescriptor.getMethod(methodName, MethodDescriptor.MethodType.UNARY);
        channel = ManagedChannelBuilder.forAddress(Configuration.get("host").asText(), Configuration.get("port").asInt()).usePlaintext().build();
        StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callback, channel);
        ClientCalls.asyncUnaryCall(channel.newCall(m, callOptions), args, observerAdpater);
      }catch (Exception e){
        logger.error("unaryRequest fail ",e.getCause());

      }
    }


  }
}
