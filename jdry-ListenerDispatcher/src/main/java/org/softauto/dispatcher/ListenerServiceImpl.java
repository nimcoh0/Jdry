package org.softauto.dispatcher;


import org.softauto.serializer.service.Message;
import org.softauto.core.Utils;
import org.softauto.serializer.service.SerializerService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ListenerServiceImpl implements SerializerService,SerializerService.Callback{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);

    @Override
    public void execute(Message message, org.softauto.serializer.CallFuture<Object> callback) throws Exception {
        Object methodResponse = null;

        try {
            logger.debug("execute message "+ message.toJson());
            String fullServiceClassName = message.getService();
            Object serviceImpl;
            Object o = ListenerObserver.getInstance().getLastChannel(message.getDescriptor());
            if(o == null) {
                logger.debug(message.getDescriptor() +" not found in Observer . trying to find constructor for " + fullServiceClassName + "$" + message.getDescriptor());
                serviceImpl = ListenerObserver.getInstance().getLastChannel(fullServiceClassName);
                Class<?>[] classes = serviceImpl.getClass().getClasses();
                Class subClass = Utils.getSubClass(classes,fullServiceClassName+"$"+message.getDescriptor());
                o = subClass.newInstance();
                if(o != null){
                    logger.debug("found constructor for " + fullServiceClassName + "$" + message.getDescriptor());
                }else {
                    logger.error("no subClass found for " + fullServiceClassName + "$" + message.getDescriptor());
                    throw new Exception("no subClass found for " + fullServiceClassName + "$" + message.getDescriptor());
                }
            }
            Method method = Utils.getMethod2(o, message.getDescriptor(), message.getTypes());
            if(method == null){
                logger.error("no method found for "+ message.getDescriptor() + " types " + message.getTypes()+ " on "+ fullServiceClassName);
            }
            logger.debug("invoking " + message.getDescriptor());
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                methodResponse = method.invoke(null, message.getArgs());
            } else {
                methodResponse = method.invoke(o, message.getArgs());
            }


        } catch (InvocationTargetException e) {
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
            logger.debug("execute message "+ message.toJson());
            String fullServiceClassName = message.getService();
            Object serviceImpl;
            Object o = ListenerObserver.getInstance().getLastChannel(message.getDescriptor());
            if(o == null) {
                logger.debug(message.getDescriptor() +" not found in Observer . sending message to trying to tests.infrastructure.ListenerServiceImpl");
                o = ListenerObserver.getInstance().getChannel("tests.infrastructure.ListenerServiceImpl");
               // o = Utils.findClass("tests.infrastructure.ListenerServiceImpl").newInstance();
                //Class<?>[] classes = serviceImpl.getClass().getClasses();
                //Class subClass = Utils.getSubClass(classes, fullServiceClassName + "$" + message.getDescriptor());
                //o = subClass.newInstance();
                if(o == null){
                    logger.error("fail getting class tests.infrastructure.ListenerServiceImpl");
                    throw new Exception("fail getting class tests.infrastructure.ListenerServiceImpl");
                }

            }
            Method  method = Utils.getMethod2(o, message.getDescriptor(), message.getTypes());
            if(method == null ){
                logger.error("no method found for "+ message.getDescriptor() + " types " + message.getTypes()+ " on "+ fullServiceClassName);
                throw new Exception("method not found for "+message.getDescriptor());
            }
            logger.debug("invoking " + message.getDescriptor());
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                methodResponse = method.invoke(null, message.getArgs());
            } else {
                methodResponse = method.invoke(o, message.getArgs());
            }


        } catch (InvocationTargetException e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );

        } catch (Exception e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );

        }
        return methodResponse;

    }


}
