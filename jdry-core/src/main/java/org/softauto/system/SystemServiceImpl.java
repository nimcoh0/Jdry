package org.softauto.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.*;
import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.Utils;
import org.softauto.jvm.HeapHelper;
import org.softauto.logger.impl.ListenerServiceImpl;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * System service for internal messages between the listener server and the grpc server
 */
public class SystemServiceImpl {

    private static SystemServiceImpl systemServiceImpl = null;
    public Injector injector ;

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
       // injector = Guice.createInjector(new BasicModule());
    }

    public int hello() {
        return 0;
    }


    public Injector getInjector() {
        return injector;
    }

    /**
     * set start test log
     * @param testname
     */
    public void startTest(String testname){
        logger.info(" **************** start test "+ testname+ " ******************");
        logger.info(TRACER," **************** start test "+ testname+ " ******************");
        //LogManager.setStatus(true);

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
            //LogManager.setStatus(false);

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
            //Log4j2Utils.changeLogLevel(Configuration.get("log_level").asText());
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
            HeapHelper.clean();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void setConnection(boolean status){
        logger.debug("set listener client connection to "+ status);
    }

    private  void load()  {
        try {
            initGuice();
            initLoger();
            //RpcProviderImpl.getInstance().initilize();
            loadPlugins();
            //InjectorInit.getInstance().initilize().register();
            loadHeapHelper();
            //initLogger();
            setConnection(true);
            logger.info(Context.JDRY,"Injector Load successfully ");
        }catch(Exception e){
            logger.fatal("init fail ",e);
            System.exit(1);
        }
    }




    private  void loadHeapHelper(){
        try {
            String path = System.getenv("temp");
            String name = "libHeapHelper.dll";
            loadLib(path,name);
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            VirtualMachine jvm = VirtualMachine.attach(pid);
            jvm.loadAgentPath(path+"/"+name, null);
            logger.info("HeapHelper agent attach to pid " + pid + " successfully");
        }catch (Exception e){
            logger.error("HeapHelper agent fail to load",e);
        }
    }

    private void loadLib(String path, String name) {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(name);
        OutputStream outputStream = null;
        try {
            File fileOut = new File(path+"/"+name);
            outputStream = new FileOutputStream(fileOut);
            IOUtils.copy(input, outputStream);
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private  void loadPlugins(){
        try {
            for(PluginProvider provider : ProviderManager.providers()){
                if(!provider.getName().equals("RPC")) {
                    provider.create().initilize().register();
                    logger.info("plugin " + provider.getName() + "load successfully");
                }
            }
        }catch (Exception e){
            logger.error("fail to load plugins  ",e);
        }
    }


    public  void initGuice(){
        try {
            Class guiceModule = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), "StepServiceModule", Configuration.get(Context.TEST_MACHINE).asText());
            AbstractModule module = (AbstractModule) guiceModule.newInstance();
            //Injector oldInjector = Guice.createInjector(allYourOtherModules);
            //Module myModule = new PropertiesModule(injector..get.get(Properties.class));
            injector =  Guice.createInjector(module);
            //injector = injector.createChildInjector(module);
            //injector = Guice.createInjector(module);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initLoger() throws IOException {
        try {
            ListenerServiceImpl listenerServiceImpl = new ListenerServiceImpl();
            Class ifaceLog = Utils.getRemoteOrLocalClass(org.softauto.core.Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), Context.LISTENER_SERVICE_LOG, org.softauto.core.Configuration.get(Context.TEST_MACHINE).asText());
            listenerServiceImpl.setIface(ifaceLog);
            org.softauto.logger.impl.Logger.init(listenerServiceImpl);
            logger.info("successfully load Logger");
        }catch (Exception e){
            logger.error("fail to load logger",e);
        }

    }
}
