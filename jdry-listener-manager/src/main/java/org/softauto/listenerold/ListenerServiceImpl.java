package org.softauto.listenerold;

import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.listener.ListenerService;
import org.softauto.listener.ServiceType;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

public class ListenerServiceImpl implements ListenerService {


    private  final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerServiceImpl.class);
    Class iface;
    String servicename = "tests.infrastructure.ListenerService";

    public ListenerServiceImpl setServicename(String servicename) {
        this.servicename = servicename;
        return this;
    }

    public ListenerServiceImpl setIface(Class iface) {
        this.iface = iface;
        return this;
    }



    @Override
    public Object[] executeBefore(String methodName, Object[] args, Class[] types) throws Exception {
        Object result = null;
        try {

                Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asText()).setPort(Configuration.get(Context.LISTENER_PORT).asInt()).buildChannel();
                Message message = Message.newBuilder().setService(servicename).setService(ServiceType.BEFORE.toString()).setDescriptor(methodName).setArgs(args).setTypes(types).build();
                result = serializer.write(message);
                logger.debug("send message successfully " + methodName);

        }catch (NoSuchMethodException n){
            return (new Object[]{});
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


    @Override
    public  void executeAfter(String methodName, Object[] args, Class[] types) throws Exception {
        Object result = null;
        try {

                Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asText()).setPort(Configuration.get(Context.LISTENER_PORT).asInt()).buildChannel();
                Message message = Message.newBuilder().setService(servicename).setService(ServiceType.AFTER.toString()).setDescriptor(methodName).setArgs(args).setTypes(types).build();
                result = serializer.write(message);
                logger.debug("send message successfully " + methodName);

        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);

            }
            logger.debug("send message "+methodName+" fail  ",e );
        }

    }


}
