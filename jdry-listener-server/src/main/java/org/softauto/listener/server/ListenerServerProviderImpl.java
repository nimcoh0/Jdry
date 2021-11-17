package org.softauto.listener.server;

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




    private ListenerServerProviderImpl(){
        objectMapper = new ObjectMapper(new YAMLFactory());
    }


    public static ListenerServerProviderImpl getInstance(){
        if(listenerProviderImpl == null){
            listenerProviderImpl =  new ListenerServerProviderImpl();
         }
        return listenerProviderImpl;
    }



    /**
     * load configuration from Configuration.yaml . set Configuration class
     * @return this
     */
    public ListenerServerProviderImpl loadConfiguration()  {
       try {
            if(new File(System.getProperty("user.dir")+"/Configuration.yaml").isFile()) {
                Configuration.setConfiguration(objectMapper.readTree(new File(System.getProperty("user.dir") + "/Configuration.yaml")));
            }
            Configuration.put(Context.TEST_MACHINE,Utils.getMachineIp());
            Configuration.put(Context.TEST_MACHINE_NAME,Utils.getMachineName());
            if(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH) == null) {
               Configuration.put(Context.TEST_INFRASTRUCTURE_PATH, System.getProperty("user.dir") + "/target/test-classes/tests/infrastructure");
            }
            if(Configuration.get(Context.LISTENER_PORT) == null) {
               Configuration.put(Context.LISTENER_PORT,"9091");
            }
            if(Configuration.get(Context.SERIALIZER_PORT) == null) {
               Configuration.put(Context.SERIALIZER_PORT,"8085");
            }
           if(Configuration.get(Context.SERIALIZER_HOST) == null) {
               Configuration.put(Context.SERIALIZER_HOST,"localhost");
           }
           if(Configuration.get(Context.ENABLE_SESSION) == null){
               Configuration.put(Context.ENABLE_SESSION,true);
           }
           if(Configuration.get(Context.LOAD_WEAVER) == null){
               Configuration.put(Context.LOAD_WEAVER,true);
           }
           logger.debug("configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
         return this;
    }




    public ListenerServerProviderImpl initialize()  {
        try {
            loadConfiguration();
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInt())
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, ListenerServiceImpl.class))
                    .build();
            server.start();
            logger.info("listener server load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asInt());
        }catch (Exception e) {
            logger.error("start Listener server fail  ", e);
        }

        return this;
    }



    public void register() {
        ServiceLocator.getInstance().register("LISTENER-SERVER",server);

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
