package org.softauto.system;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Configuration;
import org.softauto.injector.InjectorProviderImpl;
import org.softauto.jvm.JvmProviderImpl;
import org.softauto.listener.manager.ListenerClientProviderImpl;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        logger.info(" **************** start test "+ testname+ " ******************");
        logger.info(TRACER," **************** start test "+ testname+ " ******************");

    }

    /**
     * set end test log
     * @param testname
     */
    public void endTest(String testname){
        try {
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
    public int configuration(JsonNode configuration) {
        Configuration.setConfiguration(configuration);
        if(!loaded) {
            load();
            loaded = true;
        }else {
            setConnection(true);
        }
        return 0;
    }


    /**
     * shutdown at the test end
     */
    public void shutdown() {
        try {
            setConnection(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void setConnection(boolean status){
        logger.debug("set listener client connection to "+ status);
    }

    private  void load()  {
        try {
            InjectorProviderImpl.getInstance().initialize().register();
            ListenerClientProviderImpl.getInstance().initialize().register();
            JvmProviderImpl.getInstance().initialize().register();
            setConnection(true);
        }catch(Exception e){
            logger.fatal("init fail ",e);
            System.exit(1);
        }
    }



}
