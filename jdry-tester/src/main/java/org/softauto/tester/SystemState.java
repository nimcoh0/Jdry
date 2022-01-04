package org.softauto.tester;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.softauto.core.*;
import org.softauto.listener.server.ListenerObserver;
import org.softauto.listener.server.ListenerServerProviderImpl;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;


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
            int result = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_configuration", new Object[]{Configuration.getConfiguration()}, new Class[]{HashMap.class});
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
        ListenerObserver.getInstance().reset();
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
            if(new File(System.getProperty("user.dir")+ "/src/test/resources/schema/StepService.avpr").isFile()) {
                JsonNode stepService = new ObjectMapper().readTree(new File(System.getProperty("user.dir") + "/src/test/resources/schema/StepService.avpr"));
                String json = new ObjectMapper().writeValueAsString(stepService);
                Configuration.put(Context.STEP_SERVICE, json);
            }
            if(new File(System.getProperty("user.dir")+ "/src/test/resources/schema/ListenerService.avpr").isFile()) {
                File listenerService =  new File(System.getProperty("user.dir")+ "/src/test/resources/schema/ListenerService.avpr");
                //JsonNode listenerService = new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true).readTree(new File(System.getProperty("user.dir") + "/src/test/resources/schema/ListenerService.avpr"));
                //String json = new ObjectMapper().writeValueAsString(listenerService);
                Configuration.put(Context.LISTENER_SERVICE, listenerService);
            }
            if(new File(System.getProperty("user.dir")+ "/target/generated-sources/tests/infrastructure/ListenerService.java").isFile()) {
                Class listenerService = Class.forName("tests.infrastructure.ListenerService");
                HashMap<String,Object> listeners = new HashMap<>();
                Method[] methods = listenerService.getDeclaredMethods();
                for(Method method : methods){
                    listeners.put(method.getName(),method.getParameterTypes());
                }
                Configuration.put(Context.LISTENERS, listeners);
            }

            logger.debug("configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
        return this;
    }

    public SystemState addListener(String fqmn,Class...types)throws Exception{
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_addListener", new Object[]{fqmn,types}, new Class[]{String.class,Class[].class});
        return this;
    }

    public SystemState resetListeners()throws Exception{
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_resetListeners", new Object[]{}, new Class[]{});
        return this;
    }

    public SystemState removeListener(String fqmn,Class...types)throws Exception{
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_removeListener", new Object[]{fqmn,types}, new Class[]{String.class,Class[].class});
        return this;
    }
}
