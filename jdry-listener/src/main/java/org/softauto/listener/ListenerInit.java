package org.softauto.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.listener.system.SystemService;
import org.softauto.listener.system.SystemServiceImpl;

import java.io.File;
import java.io.IOException;


/**
 * listener server impl class.
 *
 */
public class ListenerInit {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerInit.class);
    private static ListenerInit listenerProviderImpl = null;
    ObjectMapper objectMapper;


    /**
     * instance of lifecycle class
     */
    LifeCycle lifeCycle;


    /**
     * listener server
     */
    Server server = null;


    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }


    private Class getListenerServiceLogImplClass() {
        return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() +"/ListenerServiceLogImpl");
    }

    private ListenerInit(){
        objectMapper = new ObjectMapper(new YAMLFactory());
    }


    public static ListenerInit getInstance(){
        if(listenerProviderImpl == null){
            listenerProviderImpl =  new ListenerInit();
         }
        return listenerProviderImpl;
    }

    private Class getListenerServiceImplClass() throws Exception{
         return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() + "/ListenerServiceImpl");
    }

    /**
     * load configuration from Configuration.yaml . set Configuration class
     * @return this
     */
    public ListenerInit loadConfiguration()  {
       try {
            if(new File(System.getProperty("user.dir")+"/Configuration.yaml").isFile()){
                Configuration.setConfiguration(objectMapper.readTree(new File(System.getProperty("user.dir")+"/Configuration.yaml")));
                Configuration.put(Context.TEST_MACHINE,Utils.getMachineIp());
                Configuration.put(Context.TEST_MACHINE_NAME,Utils.getMachineName());
            }
           logger.debug("configuration load successfully ");
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
         return this;
    }


    /**
     * initilize listener server . load Listener Service, Listener Service Log & System Service
     * @return
     * @throws IOException
     */
    public ListenerInit initilize()  {
        try {
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInt())
                    .addService(ListenerGrpcServer.createServiceDefinition(getListenerServiceClass(),  getListenerServiceImplClass().newInstance()))
                    .addService(ListenerGrpcServer.createServiceDefinition(getListenerServiceLogClass(),  getListenerServiceLogImplClass().newInstance()))
                    .addService(ListenerGrpcServer.createServiceDefinition(SystemService.class,  new SystemServiceImpl()))
                    .build();
            server.start();
        logger.info("listener load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asInt());

        }catch (Exception e) {
            logger.error("start Listener fail  ", e);
        }

        return this;
    }

    public ListenerInit register() {
        ServiceLocator.getInstance().register("LISTENER",server);
        return this;
    }


    private Class getListenerServiceClass() throws Exception{
           return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() +"/ListenerService");
    }

    private Class getListenerServiceLogClass() throws Exception{
           return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() + "/ListenerServiceLog");
     }

    public void startLifeCycle(){
        lifeCycle =  new LifeCycle();
    }

    public void shutdown(){
        lifeCycle.shutdown();
        server.shutdown();
    }

    public void keepAlive(){
        lifeCycle.keepAlive();
    }



    public void startTest(String testname){
        lifeCycle.startTest(testname);
    }

    public void endTest(String testname){
        lifeCycle.endTest(testname);
    }
}
