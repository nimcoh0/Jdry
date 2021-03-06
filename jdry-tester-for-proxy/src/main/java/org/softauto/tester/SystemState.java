package org.softauto.tester;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.softauto.core.*;
import org.softauto.listener.server.ListenerServerProviderImpl;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class SystemState {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemState.class);

    private static SystemState systemState = null;

    private SystemState(){};
    Yaml yaml = new Yaml();
    static ObjectMapper objectMapper;

    public static SystemState getInstance(){
        if(systemState == null){
            systemState = new SystemState();
            objectMapper = new ObjectMapper(new YAMLFactory());
            loadDefaultConfiguration();
        }
        return systemState;
    }


    public void initialize() throws IOException {
        loadConfiguration();
        sayHello(res->{
           if(res.succeeded()){
               logger.debug("successfully say hello");
               sendConfiguration(result ->{
                   if(result.succeeded()){
                       logger.debug("successfully send configuration");
                   }else {
                       logger.error("fail send configuration ",result.cause());
                   }
               });
           }else {
               logger.error("fail say hello ",res.cause());
           }
        });
    }


    private void sayHello(Handler<AsyncResult<Integer>> resultHandler){
        try {
            int result = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_hello", new Object[]{}, new Class[]{});
            if (Integer.valueOf(result) == 0) {
                resultHandler.handle(Future.handleResult(Integer.valueOf(result)));
            } else
                resultHandler.handle(Future.handleError(new Exception("fail sayHello")));
        }catch (Exception e){
            logger.error("fail sayHello",e);
        }
    }

    public void sendConfiguration(Handler<AsyncResult<Integer>> resultHandler){
        try {
            Context.setTestState(TestLifeCycle.INITIALIZE);
            int result = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_configuration", new Object[]{Configuration.getConfiguration()}, new Class[]{JsonNode.class});
            if (Integer.valueOf(result) == 0) {
                resultHandler.handle(Future.handleResult(Integer.valueOf(result)));
            } else
                resultHandler.handle(Future.handleError(new Exception("fail send configuration")));
        }catch (Exception e){
            logger.error("fail send configuration",e);
        }
    }

    public void shutdown(Handler<AsyncResult<Boolean>> resultHandler) throws Exception{
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_shutdown", new Object[]{}, new Class[]{});
        resultHandler.handle(Future.handleResult(true));
    }

    public void startTest(String testname,Handler<AsyncResult<Boolean>> resultHandler)throws Exception{
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_startTest", new Object[]{testname}, new Class[]{String.class});
        Context.setTestState(TestLifeCycle.START);
        resultHandler.handle(Future.handleResult(true));
    }

    public void endTest(String testname,Handler<AsyncResult<Boolean>> resultHandler)throws Exception{
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_endTest", new Object[]{testname}, new Class[]{String.class});
        Context.setTestState(TestLifeCycle.STOP);
        ListenerServerProviderImpl.getInstance().shutdown();
        resultHandler.handle(Future.handleResult(true));
    }

    public static void loadDefaultConfiguration()  {
        try {
               Configuration.setConfiguration(DefaultConfiguration.getConfiguration());


            logger.debug("default configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load default configuration ",e);
        }
    }

    public SystemState loadConfiguration()  {
        try {
            if(new File(System.getProperty("user.dir")+ "/Configuration.yaml").isFile()) {
                HashMap<String, Object> map = (HashMap<String, Object>) yaml.load(new FileReader(System.getProperty("user.dir") + "/Configuration.yaml"));
                //JsonNode userConfiguration = objectMapper.readTree(new File(System.getProperty("user.dir") + "/Configuration.yaml"));
                HashMap<String,Object> defaultConfiguration = Configuration.getConfiguration();
                defaultConfiguration.putAll(map);
                Configuration.setConfiguration(defaultConfiguration);
                //defaultConfiguration.forEach((key, value) -> map.merge(key, value, (v1, v2) -> defaultConfiguration.put(v1.getId(),v2.getName())));
                // Configuration.setConfiguration(map);
                // Configuration.setConfiguration(new MargeJsonNode().mergeNode((ObjectNode) Configuration.getConfiguration(),(ObjectNode) userConfiguration));
            }
            logger.debug("configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
        return this;
    }
}
