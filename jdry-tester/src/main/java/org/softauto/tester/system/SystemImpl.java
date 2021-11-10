package org.softauto.tester.system;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.core.Configuration;
import org.softauto.core.ServiceLocator;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

import javax.lang.model.element.Element;
import java.io.IOException;

public class SystemImpl  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemImpl.class);
    SystemService systemService = null;
    /**
     * this provider name
     */
    String type = "SYSTEM";

    /**
     * the schema interface class
     */
    Class iface;


    private static SystemImpl systemImpl = null;




    public static SystemImpl getInstance(){
        if(systemImpl == null){
            systemImpl =  new SystemImpl();
        }
        return systemImpl;
    }



    public SystemImpl initilize() throws IOException {
        systemService = SystemClient.create(SystemService.class);
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
            return this;
    }




    public void shutdown() {
        systemService.org_softauto_system_SystemServiceImpl_shutdown();
    }


    public String getType() {
        return type;
    }




    public SystemImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }


    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {
        try {
            logger.debug("system exec rpc call "+ methodName);
            Serializer   serializer = new Serializer().setChannel(channel);
            Message message = Message.newBuilder().setDescriptor(methodName).setArgs((Object[]) args[0]).setTypes((Class[]) args[1]).build();
            serializer.write(message,callback);
        }catch (Exception e){
            logger.error("fail system exec rpc call "+ methodName, e);
        }
    }

    public int sayHello() {

        return systemService.org_softauto_system_SystemServiceImpl_hello();
    }

    public void keepAlive(){
        systemService.org_softauto_system_SystemServiceImpl_keepAlive();
    }

    public void startTest(String testname){
        systemService.org_softauto_system_SystemServiceImpl_startTest(testname);
    }

    public void endTest(String testname){
        systemService.org_softauto_system_SystemServiceImpl_endTest(testname);
    }

    /**
     * send configuration to the grpc server in the sut
     * @return
     */
    public int sendConfiguration(){
        int i = -1;
        try {
            i = systemService.org_softauto_system_SystemServiceImpl_configuration(Configuration.getConfiguration());
        }catch (Exception e){
            logger.error("fail send configuration ", e);
            return 1;
        }
        return i;
    }




}
