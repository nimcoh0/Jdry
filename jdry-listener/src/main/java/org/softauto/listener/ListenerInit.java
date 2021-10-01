package org.softauto.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.serializer.service.SerializerService;

import java.io.File;


/**
 * listener server impl class.
 *
 */
public class ListenerInit {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerInit.class);
    private static ListenerInit listenerProviderImpl = null;
    ObjectMapper objectMapper;
    Injector injector;



    /**
     * listener server
     */
    Server server = null;


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

/*
    public ListenerInit initilize()  {
        try {
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInt())
                    .addService(ListenerGrpcServer.createServiceDefinition(getListenerServiceClass(),  getListenerServiceImplClass().newInstance()))
                    .addService(ListenerGrpcServer.createServiceDefinition(getListenerServiceLogClass(),  getListenerServiceLogImplClass().newInstance()))
                    .addService(ListenerGrpcServer.createServiceDefinition(SystemService.class,  new SystemServiceImpl()))
                    .addService(ListenerGrpcServer.createServiceDefinition(SerializerService.class,  new SerializerServiceImpl()))
                    .build();
            server.start();
        logger.info("listener load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asInt());

        }catch (Exception e) {
            logger.error("start Listener fail  ", e);
        }

        return this;
    }
*/


    public ListenerInit initilize()  {
        try {
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInt())
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, new org.softauto.listener.SerializerServiceImpl()))
                    .build();
            server.start();
            logger.info("listener load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asInt());
            init();
        }catch (Exception e) {
            logger.error("start Listener fail  ", e);
        }

        return this;
    }

    public Injector getInjector() {
        return injector;
    }

    protected void init() {
        try {
            Class listenerModule = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), Configuration.get(Context.LISTENER_MODULE).asText(), Configuration.get(Context.TEST_MACHINE).asText());
            AbstractModule module = (AbstractModule) listenerModule.newInstance();
            injector = Guice.createInjector(module);
        }catch (Exception e){
            e.printStackTrace();
        }
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



    public void shutdown(){
       server.shutdown();
    }

}
