package org.softauto.listener.client;

import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

public class ListenerServiceImpl {


    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerServiceImpl.class);


    public static Object[] execute(String methodName, Object[] args, Class[] types) throws Exception {
        Object result = null;
        try {
            if (ListenerClientProviderImpl.getInstance().getIface().getMethod(methodName, types) != null) {
                Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asText()).setPort(Configuration.get(Context.LISTENER_PORT).asInt()).buildChannel();
                Message message = Message.newBuilder().setDescriptor(methodName).setArgs(args).setTypes(types).build();
                result = serializer.write(message);
                logger.debug("send message successfully " + methodName);
            }
        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);
                return (new Object[]{});
            }
            logger.debug("send message "+methodName+" fail  ",e );
        }
        if(result == null){
            return (new Object[]{});
        }
        if(result instanceof Object[]){
            return (Object[])result;
        }
        return new Object[]{result};
    }




}
