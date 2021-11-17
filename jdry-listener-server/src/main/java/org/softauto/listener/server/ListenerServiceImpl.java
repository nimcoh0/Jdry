package org.softauto.listener.server;


import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.serializer.service.Message;
import org.softauto.core.Utils;
import org.softauto.serializer.service.SerializerService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ListenerServiceImpl implements SerializerService{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);
    private Class listener = null;

    public ListenerServiceImpl(){
        this.listener =  Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , "Listener", Configuration.get(Context.TEST_MACHINE).asText());
    }


    @Override
    public Object execute(Message message) throws Exception {
        Object methodResponse = null;
        try {
            logger.debug("execute message "+ message.toJson());
            String fullServiceClassName = message.getService();
            Object o = ListenerObserver.getInstance().getLastChannel(message.getDescriptor());
            if(o == null) {
                logger.debug(message.getDescriptor() +" not found in Observer . using "+message.getDescriptor());
                o = Utils.getSubClass(listener.getDeclaredClasses(),"tests.infrastructure.Listener$"+message.getDescriptor()).newInstance();
                if(o == null){
                    logger.error("fail getting class "+message.getDescriptor());
                    throw new Exception("fail getting class "+message.getDescriptor());
                }

            }
            Method  method = Utils.getMethod2(o, message.getDescriptor(), message.getTypes());
            if(method == null ){
                logger.error("no method found for "+ message.getDescriptor() + " types " + message.getTypes()+ " on "+ fullServiceClassName);
                throw new Exception("method not found for "+message.getDescriptor());
            }
            logger.debug("invoking " + message.getDescriptor()+" args "+ Utils.result2String(message.getArgs()));
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
