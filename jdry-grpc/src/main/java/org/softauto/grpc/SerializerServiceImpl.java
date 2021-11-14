package org.softauto.grpc;


import org.softauto.core.AbstractInjector;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.system.SystemServiceImpl;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class SerializerServiceImpl implements SerializerService,SerializerService.Callback{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SerializerServiceImpl.class);


    @Override
    public void execute(Message message, CallFuture<Object> callback) throws Exception {
        Object methodResponse = null;
        try {
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            Object serviceImpl;
            AbstractInjector injector = (AbstractInjector)ServiceLocator.getInstance().getService("INJECTOR");
            if(injector != null) {
                serviceImpl = injector.inject(fullClassName);
            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, message.getArgs());
            } else {
                methodResponse = m.invoke(serviceImpl, message.getArgs());
            }


        } catch (
            InvocationTargetException e) {
                logger.error("fail invoke method "+ message.getDescriptor(),e );
                callback.handleError(e);
            } catch (Exception e) {
                logger.error("fail invoke method "+ message.getDescriptor(),e );
                callback.handleError(e);
              }
            callback.handleResult(methodResponse);
    }

    @Override
    public Object execute(Message message) throws Exception {
        Object methodResponse = null;
        try {
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            Object serviceImpl;
            AbstractInjector injector = (AbstractInjector)ServiceLocator.getInstance().getService("INJECTOR");
            if(injector != null && !fullClassName.equals("org.softauto.system.SystemServiceImpl")) {
                serviceImpl = injector.inject(fullClassName);
            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            String methodName = Utils.getMethodName(message.getDescriptor());
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, message.getArgs());
            } else {
                methodResponse = m.invoke(serviceImpl, message.getArgs());
            }


        } catch (
                InvocationTargetException e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            methodResponse = e.getTargetException();

        } catch (Exception e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            methodResponse = e;
        }
        return methodResponse;
    }
}
