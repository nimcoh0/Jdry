package org.softauto.tester;

import io.grpc.CallOptions;
import org.apache.avro.Protocol;
import org.softauto.core.Utils;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * generic client that get the request and execute it using the correct protocol provider
 * using the data from the protocol schema, can execute async & sync request.
 * this client is base on AVRO grpc client impl. for more detail on avro client see avro doc for grpc .
 * for the differents between this impl and avro see wiki .
 */
public class Client {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Client.class);


    /**
     * @param iface interface for which client is built
     * @param <T>   type of Interface
     * @return a new client proxy.
     * @throws Exception
     */
    public static <T> T create(Class<T> iface) throws Exception {
        try {
            return create(iface, CallOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("fail create tester proxy ", e);
        }
        throw new Exception("fail create tester proxy ");
    }


    private static <T> T create(Class<T> iface, CallOptions callOptions) {
        T t = null;
        Protocol protocol = Utils.getProtocol(iface);
        t = (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, (proxy, method, methodArgs) -> {
            return new ServiceInvocationHandler(callOptions, protocol, iface).invoke(proxy, method, methodArgs);
        });
        logger.debug("tester proxy instance create successfully ");
        return t;
    }

    private static class ServiceInvocationHandler implements InvocationHandler {
        private CallOptions callOptions;
        Protocol protocol;
        Class<?> iface;

        ServiceInvocationHandler(CallOptions callOptions, Protocol protocol, Class<?> iface) {
            this.callOptions = callOptions;
            this.protocol = protocol;
            this.iface = iface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                if (args == null) {
                    args = new Object[]{};
                }
                logger.debug("invoke method " + method.getName() + " with args " + Arrays.toString(args));
                return invokeUnaryMethod(method, args);
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
            }
            return null;
        }


        private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.softauto.serializer.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                // get the callback argument from the end
                logger.debug("invoke async request :" + method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                org.softauto.serializer.CallFuture<?> callback = (org.softauto.serializer.CallFuture<?>) args[args.length - 1];
                unaryRequest(method.getName(), callback, finalArgs, finalTypes);
                return null;
            } else {
                logger.debug("invoke sync request :" + method.getName());
                return unaryRequest(method.getName(), args, parameterTypes);

            }
        }

        private Object unaryRequest(String methodName, Object... args) throws Exception {
            org.softauto.serializer.CallFuture<?> callFuture = new org.softauto.serializer.CallFuture<>();
            unaryRequest(methodName, callFuture, args);
            return callFuture.get();
        }


        private <RespT> void unaryRequest(String methodName, org.softauto.serializer.CallFuture<RespT> callback, Object... args) throws Exception {
            Protocol.Message message = protocol.getMessages().get(methodName);
            logger.debug("invoking message according to :" + message.toString());
            Provider provider = ProviderManager.provider(message.getProp("transceiver")).create().iface(iface);
            logger.debug("invoke method " + methodName + " using protocol " + message.getProp("transceiver"));
            provider.exec(methodName, callback, null, args);
        }

    }
}
