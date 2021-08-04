package org.softauto.grpc.listener;

import org.apache.avro.Protocol;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.Utils;
import org.softauto.grpc.SoftautoGrpcUtils;

import java.lang.reflect.Method;

/**
 * helper class to send messages to the tester
 */
public class Sender {


    public static Builder newBuilder() { return new Builder();}
    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Sender.class);
    Object[] args;
    ListenerGrpcClient.ServiceInvocationHandler proxy;
    Method method;
    String fqmn;

    public Sender(ListenerGrpcClient.ServiceInvocationHandler proxy,Method method ,Object[] args,String fqmn){
        this.args = args;
        this.method = method;
        this.proxy = proxy;
        this.fqmn = fqmn;
    }

    public Object[] send() {
        Object result = null;
        try {
            result = proxy.invoke(null, method, args);
            logger.debug("send message successfully "+fqmn);
        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);
                return (new Object[]{});
            }
            logger.debug("send message "+fqmn+" fail  ",e );
        }
        if(result == null){
            return (new Object[]{});
        }
        if(result instanceof Object[]){
            return (Object[])result;
        }
        return new Object[]{result};
    }

    public static class Builder {
        private String fqmn;
        private Object[] objs;
        private Class[] types;
        private Object[] args;
        private ListenerGrpcClient.ServiceInvocationHandler proxy;

        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Builder.class);

        public Builder(){

        }

        public String getFqmn() {
            return fqmn;
        }

        public Builder setFqmn(String fqmn) {
            this.fqmn = fqmn;
            return this;
        }

        public Object[] getObjs() {
            return objs;
        }

        public Builder setObjs(Object[] objs) {
            this.objs = objs;
            return this;
        }



        public Sender build(){
            try {
                types = Utils.getTypes(objs[1]);
                args = Utils.getArgs(objs[0]);
                Class listenerServiceImplClass = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Context.LISTENER_SERVICE_IMPL,Configuration.get(Context.TEST_MACHINE).asText());
                Class listenerServiceLogImplClass = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Context.LISTENER_SERVICE_LOG_IMPL,Configuration.get(Context.TEST_MACHINE).asText());
                Object listenerServiceImpl = listenerServiceImplClass.newInstance();
                Object listenerServiceLogImpl = listenerServiceLogImplClass.newInstance();
                Class c = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Context.LISTENER_SERVICE,Configuration.get(Context.TEST_MACHINE).asText());
                Class log = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Context.LISTENER_SERVICE_LOG,Configuration.get(Context.TEST_MACHINE).asText());
                Protocol protocol = SoftautoGrpcUtils.getProtocol(c);
                int index = Utils.isGeneric(protocol,fqmn);
                if(index != -1){
                    types[index] = Object.class;
                }
                Method m = Utils.getMethod2(listenerServiceImpl, fqmn, types);
                proxy = ListenerGrpcClient.create(c);
                if (m == null) {
                    proxy = ListenerGrpcClient.create(log);
                    m = Utils.getMethod2(listenerServiceLogImpl, fqmn, types);
                }
                if(m != null){
                   return  new Sender(proxy,m , args,fqmn );
                }
            }catch (Exception e){
                logger.debug("build message "+fqmn+" fail  ",e );
            }
            return null;
        }

    }

}
