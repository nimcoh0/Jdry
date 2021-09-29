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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.MethodDescriptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import org.apache.avro.Protocol;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.Utils;
import org.softauto.grpc.SoftautoGrpcServer;
import org.softauto.grpc.SoftautoGrpcUtils;
import org.softauto.grpc.SystemServer;
import org.softauto.listener.system.BasicModule;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;


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
  public static ServerServiceDefinition createServiceDefinition(Class iface) {
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
        //if (msg != null && msg.getProp("transceiver").equals("RPC")) {
          try {
            UnaryMethodHandler methodHandler = null;
            methodHandler = new UnaryMethodHandler();
            serviceDefinitionBuilder.addMethod(
                    serviceDescriptor.getMethod(method.getName(), MethodDescriptor.MethodType.UNARY),
                    ServerCalls.asyncUnaryCall(methodHandler));
          } catch (Exception e) {
            e.printStackTrace();
          }
        //}

      }
    }catch (Exception e){
      logger.error("ServerServiceDefinition creation fail ", e);
    }
    return serviceDefinitionBuilder.build();
  }


  private static class UnaryMethodHandler implements ServerCalls.UnaryMethod<Object[], Object> {
    Injector injector;


    UnaryMethodHandler() {

    }

    @Override
    public void invoke(Object[] request, StreamObserver<Object> responseObserver) {
      Object methodResponse = null;
      Message message = (Message) request[0];
      injector = ListenerInit.getInstance().getInjector();
      try {
        String fullClassName = message.getService();
        Class iface = Utils.findClass(fullClassName);
        Object serviceImpl = injector.getInstance(iface);
        Method method = Utils.getMethod2(serviceImpl, message.getDescriptor(), message.getTypes());
        //ListenerGrpcClient.ServiceInvocationHandler serviceInvocationHandler = ListenerGrpcClient.create(iface);
        //methodResponse = serviceInvocationHandler.invoke(iface,methods,message.getArgs());

        //Object serviceImpl = injector.getInstance(iface);
        Object channel = ListenerObserver.getInstance().getLastChannel(serviceImpl.getClass().getName());
        if(channel == null){
          channel = serviceImpl;
        }
        //Method m = Utils.getMethod2(channel, methodName, message.getTypes());
        logger.debug("invoking " + message.getDescriptor());
        method.setAccessible(true);
        if (Modifier.isStatic(method.getModifiers())) {
          methodResponse = method.invoke(null, message.getArgs());
        } else {
          methodResponse = method.invoke(channel, message.getArgs());
        }


      } catch (InvocationTargetException e) {
        logger.error("fail invoke method "+ message.getDescriptor(),e );
        responseObserver.onError(e);
      } catch (Exception e) {
        logger.error("fail invoke method "+ message.getDescriptor(),e );
        responseObserver.onError(e);
      }
      responseObserver.onNext(methodResponse);
      responseObserver.onCompleted();
    }


/*
    @Override
    public void invoke(Object[] request, StreamObserver<Object> responseObserver) {


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
  private static class OneWayUnaryMethodHandler extends UnaryMethodHandler {
    private static final Logger LOG = Logger.getLogger(OneWayUnaryMethodHandler.class.getName());
    Method method;
    private final Object serviceImpl;

    OneWayUnaryMethodHandler(Object serviceImpl,Method method) {
      super(serviceImpl,method);
      this.serviceImpl = serviceImpl;
      this.method = method;
    }

    @Override
    public void invoke(Object[] request, StreamObserver<Object> responseObserver) {
      // first respond back with a fixed void response in order for call to be
      // complete

      responseObserver.onNext(null);
      responseObserver.onCompleted();
      try {
        Method m = Utils.getMethod(serviceImpl, method.getName(), method.getParameterTypes());
        logger.debug("invoking " + method );
        m.setAccessible(true);
        m.invoke(serviceImpl, request);
      } catch (Exception e) {
        logger.error("fail invoke method "+ method,e );
        Throwable cause = e;
        while (cause.getCause() != null && cause != cause.getCause()) {
          cause = cause.getCause();
        }
        LOG.log(Level.WARNING, "Error processing one-way rpc", cause);
      }
    }

 */
  }
}
