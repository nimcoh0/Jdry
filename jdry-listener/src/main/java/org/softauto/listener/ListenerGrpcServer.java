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

import io.grpc.MethodDescriptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import org.apache.avro.Protocol;
import org.softauto.core.Utils;
import org.softauto.grpc.SoftautoGrpcUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 *  listener grpc server base on avro grpc server
 *  execute listener methode on the register listeners impl classes
 */
public abstract class ListenerGrpcServer {

  private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerGrpcServer.class);

  protected ListenerGrpcServer() {


  }

  /**
   * Creates a {@link ServerServiceDefinition} for Avro Interface and its
   * implementation that can be passed a gRPC Server.
   *
   * @param iface Avro generated RPC service interface for which service defintion
   *              is created.
   * @return a new server service definition.
   */
  public static ServerServiceDefinition createServiceDefinition(Class iface,Object impl) {
    ServerServiceDefinition.Builder serviceDefinitionBuilder = null;
    try {
      Protocol protocol = SoftautoGrpcUtils.getProtocol(iface);
      ServiceDescriptor serviceDescriptor = (ServiceDescriptor) ServiceDescriptor.create(iface);
      serviceDefinitionBuilder = ServerServiceDefinition
              .builder(serviceDescriptor.getServiceName());
      Map<String, Protocol.Message> messages = protocol.getMessages();
      for (Method method : iface.getMethods()) {
        Protocol.Message msg = messages.get(method.getName());
        // setup a method handler only if corresponding message exists in avro protocol.
        if (msg != null && msg.getProp("transceiver").equals("RPC")) {
          try {
            Method m = Utils.getMethod2(impl, method.getName(), method.getParameterTypes());
            UnaryMethodHandler methodHandler = new UnaryMethodHandler(impl, m);
            serviceDefinitionBuilder.addMethod(
                    serviceDescriptor.getMethod(method.getName(), MethodDescriptor.MethodType.UNARY),
                    ServerCalls.asyncUnaryCall(methodHandler));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }
    }catch (Exception e){
      logger.error("ServerServiceDefinition creation fail ", e);
    }
    return serviceDefinitionBuilder.build();
  }

  private static class UnaryMethodHandler implements ServerCalls.UnaryMethod<Object[], Object> {
    private final Object serviceImpl;
    private final Method method;

    UnaryMethodHandler(Object serviceImpl, Method method) {
      this.serviceImpl = serviceImpl;
      this.method = method;
    }

    /**
     * invoke listener method on all register impl classes
     * @param request
     * @param responseObserver
     */
    @Override
    public void invoke(Object[] request, StreamObserver<Object> responseObserver) {
        List<Object> channels = ListenerObserver.getInstance().getChannels(serviceImpl.getClass().getName());
        AtomicReference<Object> reference = new AtomicReference();
        if (channels.size() == 0) {
          channels = new ArrayList<>();
          channels.add(serviceImpl);
          logger.debug("adding "+ serviceImpl.getClass().getName() +" to the channels list");
        }
        logger.debug("invoke method " + method.getName() + "on " + channels.size() + " channels");
        channels.forEach(channel -> {
          Object methodResponse = null;
          try {
            logger.debug("using channel: " + channel.getClass().getName());
            method.setAccessible(true);
            reference.set(method.invoke(channel, request));
            logger.debug("method " + method.getName() + " invoke successfully");
          } catch (InvocationTargetException e) {
            reference.set(e.getTargetException());
            logger.error("fail invoke listener method " + method.getName(), e);
          } catch (Exception e) {
            logger.error("fail invoke listener method " + method.getName(), e);
            logger.debug("method: " + method.getName());
            logger.debug("args: " + Arrays.toString(request));
            logger.debug("types: " + Arrays.toString(method.getParameterTypes()));
            logger.debug("channel: " + channel.getClass().getName());
            reference.set(e);
          }

        });

        responseObserver.onNext(reference.get());
        responseObserver.onCompleted();

    }
  }

}
