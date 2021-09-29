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

package org.softauto.grpc;


import com.google.inject.*;
import io.grpc.MethodDescriptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import org.apache.avro.Protocol;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Provides components to set up a gRPC Server for serialization.
 * Base on Avro
 */
public abstract class SoftautoGrpcServer {


  private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SoftautoGrpcServer.class);
  private static Injector injector ;


  protected SoftautoGrpcServer() {
    try {

    }catch (Exception e){
      e.printStackTrace();
    }
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
    Protocol protocol = SoftautoGrpcUtils.getProtocol(iface);
    ServiceDescriptor serviceDescriptor = (ServiceDescriptor) ServiceDescriptor.create(iface);
    ServerServiceDefinition.Builder serviceDefinitionBuilder = ServerServiceDefinition
        .builder(serviceDescriptor.getServiceName());
    Map<String, Protocol.Message> messages = protocol.getMessages();
    for (Method method : iface.getMethods()) {
      Protocol.Message msg = messages.get(method.getName());
      // setup a method handler only if corresponding message exists in avro protocol.
      String transceiver = getTransceiver(msg);
      String fullClassName = getFullClassName(msg,method);
      if (msg != null && transceiver.equals("RPC")) {
          try {
            UnaryMethodHandler methodHandler = null;
            methodHandler = msg.isOneWay() ? new OneWayUnaryMethodHandler(method, fullClassName, msg)
                      : new UnaryMethodHandler(method, fullClassName, msg);

            serviceDefinitionBuilder.addMethod(
                    serviceDescriptor.getMethod(method.getName(), MethodDescriptor.MethodType.UNARY),
                    ServerCalls.asyncUnaryCall(methodHandler));
          }catch (Exception e){
            e.printStackTrace();
          }
        }

    }
    return serviceDefinitionBuilder.build();
  }

 private static String getTransceiver(Protocol.Message msg){
   if(msg.hasProp("transceiver")){
     return msg.getProp("transceiver");
   }
   return "RPC";
 }

 private static String getFullClassName(Protocol.Message msg,Method method){
   if(msg.hasProp("class")){
     return ((HashMap) msg.getObjectProp("class")).get("fullClassName").toString();
   }else {
     String fqmn = method.getName().replace("_",".");
     return fqmn.substring(0,fqmn.lastIndexOf("."));
    }
 }

  protected static class UnaryMethodHandler implements ServerCalls.UnaryMethod<Object[], Object> {

    private final Method method;
    private String fullClassName;
    private Protocol.Message msg;


    UnaryMethodHandler(Method method,String fullClassName,Protocol.Message msg) throws Exception{
      this.msg = msg;
      this.method = method;
      this.fullClassName = fullClassName;
      //Class guiceModule = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), Configuration.get(Context.GUICE_MODULE).asText(), Configuration.get(Context.TEST_MACHINE).asText());
      //AbstractModule module = (AbstractModule) guiceModule.newInstance();
      //injector = Guice.createInjector(module);
    }


    @Override
    public void invoke(Object[] request, StreamObserver<Object> responseObserver) {
      Object methodResponse = null;
      try {
        Object serviceImpl = injector.getInstance(Utils.findClass(fullClassName));
        //Object serviceImpl = Utils.getClassInstance(fullClassName, msg, request, method);
        if(!msg.hasProp("type") || !msg.getProp("type").equals("constructor")) {
          Method m = Utils.getMethod(serviceImpl, method.getName(), method.getParameterTypes());
          logger.debug("invoking " + method);
          m.setAccessible(true);
          if (Modifier.isStatic(m.getModifiers())) {
            methodResponse = m.invoke(null, request);
          } else {
            methodResponse = m.invoke(serviceImpl, request);
          }
        }else {
            methodResponse = serviceImpl;
        }

      } catch (InvocationTargetException e) {
        logger.error("fail invoke method "+ method,e );
        methodResponse = e.getTargetException();
      } catch (Exception e) {
        logger.error("fail invoke method "+ method,e );
        methodResponse = e;
      }
      responseObserver.onNext(methodResponse);
      responseObserver.onCompleted();
    }

    public Method getMethod() {
      return method;
    }


  }

  private static class OneWayUnaryMethodHandler extends UnaryMethodHandler {
    private static final Logger LOG = Logger.getLogger(OneWayUnaryMethodHandler.class.getName());
    private String fullClassName;
    Method method;
    private Protocol.Message msg;

    OneWayUnaryMethodHandler(Method method,String fullClassName,Protocol.Message msg) throws Exception{
      super(method,fullClassName,msg);
      this.fullClassName = fullClassName;
      this.method = method;
      this.msg = msg;
    }


    @Override
    public void invoke(Object[] request, StreamObserver<Object> responseObserver) {
      // first respond back with a fixed void response in order for call to be
      // complete

      responseObserver.onNext(null);
      responseObserver.onCompleted();

      // process the rpc request
      try {
          Object serviceImpl = injector.getInstance(Utils.findClass(fullClassName));
          //Object serviceImpl = Utils.getClassInstance(fullClassName, msg, request, method);
          if(!msg.hasProp("type") || !msg.getProp("type").equals("constructor")) {
          Method m = Utils.getMethod(serviceImpl, msg.getProp("method"), method.getParameterTypes());
          logger.debug("invoking " + method);
          m.setAccessible(true);
          if (Modifier.isStatic(m.getModifiers())) {
            m.invoke(null, request);
          } else {
            m.invoke(serviceImpl, request);
          }
        }else {
          responseObserver.onNext(serviceImpl);

        }
      } catch (Exception e) {
        logger.error("fail invoke method "+ method,e );
        Throwable cause = e;
        while (cause.getCause() != null && cause != cause.getCause()) {
          cause = cause.getCause();
        }
        LOG.log(Level.WARNING, "Error processing one-way rpc", cause);
      }
    }
  }
}
