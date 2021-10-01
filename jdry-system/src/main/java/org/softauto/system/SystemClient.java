package org.softauto.system;

import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.avro.Protocol;
import org.softauto.core.Configuration;
import org.softauto.core.Utils;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;

public class SystemClient {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SystemClient.class);
    public static <T> T create(Class<T> iface) {
        return create(iface, CallOptions.DEFAULT);
    }


    private static <T> T create(Class<T> iface, CallOptions callOptions) {
        T t = null;
        try {
            Protocol protocol = Utils.getProtocol(iface);
            t = (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, (proxy, method, methodArgs) -> {
                return new ServiceInvocationHandler(callOptions, protocol, iface).invoke(proxy, method, methodArgs);
            });
            logger.debug("proxy instance create successfully ");
        } catch (Exception e) {
            logger.error("proxy instance create fail ", e);
        }
        return t;
    }

    public static class ServiceInvocationHandler implements InvocationHandler {
        private  CallOptions callOptions;
        Protocol protocol;
        Class<?> iface ;
        ManagedChannel channel = null;


        ServiceInvocationHandler( CallOptions callOptions, Protocol protocol ) {
            this.callOptions = callOptions;
            this.protocol = protocol;
        }

        ServiceInvocationHandler(CallOptions callOptions, Protocol protocol, Class<?> iface) {
            this.callOptions = callOptions;
            this.protocol = protocol;
            this.iface = iface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)  {
            Object obj = null;
            try {
                if(args == null){
                    args = new Object[]{};
                }
                obj =   invokeUnaryMethod(method, args);
                logger.debug("successfully invoke method "+ method.getName()+ " with args "+ Arrays.toString(args));
            } catch (Exception e) {
                logger.error("invoke method "+ method.getName()+ " with args "+ Arrays.toString(args),e);
            }
            return obj;
        }


        private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.softauto.serializer.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                // get the callback argument from the end
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                logger.debug("invoking  async request :"+ method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                org.softauto.serializer.CallFuture<?> callback = (org.softauto.serializer.CallFuture<?>) args[args.length - 1];
                unaryRequest(method.getName(),  callback,finalArgs,finalTypes);
                return null;
            } else {
                logger.debug("invoking  sync request :"+ method.getName());
                return unaryRequest(method.getName(), args,parameterTypes);
            }
        }

        private Object unaryRequest(String methodName, Object...args) throws Exception {
            org.softauto.serializer.CallFuture<Object> callFuture = new org.softauto.serializer.CallFuture<>();
            unaryRequest(methodName,  callFuture,args);
            return callFuture.get();
        }

        /**
         * this method will resolve the correct protocol provider base on the schema property  "transceiver"
         * and will call the provider impl to handel the request
         * @param methodName
         * @param args
         * @param callback
         * @param <RespT>
         * @throws Exception
         */
        private <RespT> void unaryRequest(String methodName, org.softauto.serializer.CallFuture<RespT> callback,Object...args)  {
            Provider provider = ProviderManager.provider("SYSTEM").create().iface(iface);
            String host = Configuration.get("serializer_host").asText();
            int port = Configuration.get("serializer_port").asInt();
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            provider.exec( methodName,callback, channel,args);
        }

    }
}
