package org.softauto.dispatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.grpc.SerializerServiceImpl;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.SerializerService;

import javax.lang.model.element.Element;
import java.io.File;


/**
 * listener server impl class.
 *
 */
public class ListenerServerProviderImpl implements Provider {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServerProviderImpl.class);
    private static ListenerServerProviderImpl listenerProviderImpl = null;
    ObjectMapper objectMapper;
    //Injector injector;

    /**
     * this provider name
     */
    String type = "LISTENER-SERVER";

    /**
     * the schema interface class
     */
    Class iface;

    /**
     * listener server
     */
    Server server = null;


    private Class getListenerServiceLogImplClass() {
        return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() +"/ListenerServiceLogImpl");
    }

    private ListenerServerProviderImpl(){
        objectMapper = new ObjectMapper(new YAMLFactory());
    }


    public static ListenerServerProviderImpl getInstance(){
        if(listenerProviderImpl == null){
            listenerProviderImpl =  new ListenerServerProviderImpl();
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
    public ListenerServerProviderImpl loadConfiguration()  {
       try {
            if(new File(System.getProperty("user.dir")+"/Configuration.yaml").isFile()){
                Configuration.setConfiguration(objectMapper.readTree(new File(System.getProperty("user.dir")+"/Configuration.yaml")));
                Configuration.put(Context.TEST_MACHINE,Utils.getMachineIp());
                Configuration.put(Context.TEST_MACHINE_NAME,Utils.getMachineName());
                if(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH) == null)
                    Configuration.put(Context.TEST_INFRASTRUCTURE_PATH,System.getProperty("user.dir")+"/target/test-classes/tests/infrastructure");
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


    public ListenerServerProviderImpl initilize()  {
        try {
            loadConfiguration();
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInt())
                    //.addService(ListenerGrpcServer.createServiceDefinition(SerializerService.class,new ListenerServiceImpl()))
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, ListenerServiceImpl.class))
                    .build();
            server.start();
            logger.info("listener load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asInt());
            //init();
        }catch (Exception e) {
            logger.error("start Listener fail  ", e);
        }

        return this;
    }

    /*
    public Injector getInjector() {
        return injector;
    }
*/
    /*
    protected void init() {
        try {
            Class listenerModule = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), "ListenerModule", Configuration.get(Context.TEST_MACHINE).asText());
            AbstractModule module = (AbstractModule) listenerModule.newInstance();
            injector = Guice.createInjector(module);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
*/

    public void register() {
        ServiceLocator.getInstance().register("LISTENER-SERVER",server);

    }


    private Class getListenerServiceClass() throws Exception{
           return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() +"/ListenerService");
    }

    private Class getListenerServiceLogClass() throws Exception{
           return Utils.getClazz(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() + "/ListenerServiceLog");
     }


    @Override
    public void shutdown(){
       server.shutdown();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public JsonNode parser(Element element) {
        return null;
    }

    @Override
    public ListenerServerProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }

    @Override
    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {

    }

}
