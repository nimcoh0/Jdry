package org.softauto.system;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.TestLifeCycle;
import org.softauto.injector.InjectorInitializer;
//import org.softauto.jvm.JvmProviderImpl;
import org.softauto.listener.Listeners;
import org.softauto.listener.manager.ListenerClientProviderImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * System service for internal messages between the listener server and the grpc server
 */
public class SystemServiceImpl {

    private static SystemServiceImpl systemServiceImpl = null;

    /** indecate if the syatem was loaded */
    boolean loaded = false;

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemServiceImpl.class);

    private static final Marker TRACER = MarkerManager.getMarker("TRACER");

    public static SystemServiceImpl getInstance(){
        if(systemServiceImpl == null){
            systemServiceImpl = new SystemServiceImpl();
        }
        return systemServiceImpl;
    }

    private SystemServiceImpl(){

    }

    public int hello() {
        return 0;
    }



    /**
     * set start test log
     * @param testname
     */
    public void startTest(String testname){
        Context.setTestState(TestLifeCycle.START);
        logger.info(" **************** start test "+ testname+ " ******************");
        logger.info(TRACER," **************** start test "+ testname+ " ******************");

    }

    /**
     * set end test log
     * @param testname
     */
    public void endTest(String testname){
        try {
            Context.setTestState(TestLifeCycle.STOP);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date(System.currentTimeMillis());
            String d = formatter.format(date);
            System.setProperty("logFilename", testname+"_"+d);
            logger.info(" **************** end test " + testname + " ******************");
            logger.info(TRACER, " **************** end test " + testname + " ******************");
            logger.info(TRACER, "roll test");

        }catch (Exception e){
            logger.error("fail end test ",e);
        }
    }

    /**
     * set configuration
     * @param configuration
     * @return
     */
    public int configuration(HashMap<String,Object> configuration) {
        try {
            Context.setTestState(TestLifeCycle.INITIALIZE);
            Configuration.setConfiguration(configuration);
            if (!loaded) {
                load();
                loaded = true;
            }
        }catch (Exception e){
            logger.error("fail set configuration  ",e);
        }
        logger.debug("successfully  set configuration  ");
        return 0;
    }

    public void shutdown() {

    }

    private  void load()  {
        try {
            InjectorInitializer.getInstance().initialize();
            ListenerClientProviderImpl.getInstance().initialize().register();
            //JvmProviderImpl.getInstance().initialize().register();
        }catch(Throwable e){
            logger.error("init fail ",e);
            //System.exit(1);
        }
    }

    public void addListener(String fqmn,Class...types){
        Listeners.addListener(fqmn,types);
    }

    public void addListenerMock(String fqmn,Class[] types,Object[] value){
        Listeners.addMock(fqmn,types,value);
    }

    public void resetListeners(){
        Listeners.resetListener();
    }

    public void removeListener(String fqmn){
        Listeners.removeListener(fqmn);
    }
}
