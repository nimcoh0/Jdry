package org.softauto.listener;

import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.avro.Protocol;
import org.softauto.core.CallFuture;
import org.softauto.core.Configuration;
import org.softauto.core.Utils;
import org.softauto.listener.system.SystemService;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * manage the communication lifeCycle for the system service
 */
public class LifeCycle {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(LifeCycle.class);
    SystemService systemService = null;

    /**
     * life cycle to get configuration from tester
     * if SUT answer to sayHello the configuration will be send
     * to the grpc server
     */
    public LifeCycle(){
       int result =  sayHello();
       if(result == 0){
           int res = sendConfiguration();
           if(res != 0){
               logger.fatal(" fail to send configuration to sut  ");
               System.exit(1);
           }
       }else {
           logger.fatal(" now answer for sut  ");
           System.exit(1);
       }
    }

    /**
     * check if the Sut is a live
     * @return
     */
    public int sayHello() {
        systemService = create(SystemService.class);
        return systemService.org_softauto_grpc_system_SystemServiceImpl_hello();
    }

    public void keepAlive(){
        systemService.org_softauto_grpc_system_SystemServiceImpl_keepAlive();
    }

    public void startTest(String testname){
        systemService.org_softauto_grpc_system_SystemServiceImpl_startTest(testname);
    }

    public void endTest(String testname){
        systemService.org_softauto_grpc_system_SystemServiceImpl_endTest(testname);
    }

    /**
     * send configuration to the grpc server in the sut
     * @return
     */
    public int sendConfiguration(){
        int i = -1;
        try {
           i = systemService.org_softauto_grpc_system_SystemServiceImpl_configuration(Configuration.getConfiguration());
        }catch (Exception e){
            logger.error("fail send configuration ", e);
            return 1;
        }
        return i;
    }

    /**
     * send request to shutdown to the grpc server in the Sut .indicate test/suite end
     */
    public void shutdown(){
        systemService.org_softauto_grpc_system_SystemServiceImpl_shutdown();
    }

    /**
     * create the stub proxy
     * @param iface
     * @param <T>
     * @return
     */
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
                    && CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                // get the callback argument from the end
                logger.debug("invoking  async request :"+ method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                CallFuture<?> callback = (CallFuture<?>) args[args.length - 1];
                unaryRequest(method.getName(), finalArgs, callback);
                return null;
            } else {
                logger.debug("invoking  sync request :"+ method.getName());
                return unaryRequest(method.getName(), args);
            }
        }

        private Object unaryRequest(String methodName, Object[] args) throws Exception {
            CallFuture<Object> callFuture = new CallFuture<>();
            unaryRequest(methodName, args, callFuture);
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
        private <RespT> void unaryRequest(String methodName, Object[] args, CallFuture<RespT> callback)  {
            Provider provider = ProviderManager.provider("RPC").create().iface(iface);
            String host = Configuration.get("serializer_host").asText();
            int port = Configuration.get("system_port").asInt();
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            provider.exec( methodName,  args,  callback, channel);
        }

    }
}
