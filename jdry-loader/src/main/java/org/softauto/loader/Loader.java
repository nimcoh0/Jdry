package org.softauto.loader;

import org.softauto.grpc.RpcProviderImpl;
import org.softauto.jvm.JvmProviderImpl;


import java.lang.instrument.Instrumentation;

/**
 * Java agent to load on the SUT
 * load Rpc server
 *
 */
public class Loader {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Loader.class);


    public static void agentmain(String agentArgs, Instrumentation instrumentation){
        try {
            RpcProviderImpl.getInstance().initilize().register();
            JvmProviderImpl.getInstance().initilize().register();

        }catch(Exception e){
            logger.fatal("jdry agent attach to vm fail ",e);
            System.exit(1);
        }
    }

    /**
     * load agent and set the transformer
     * @param agentArgument
     * @param instrumentation
     */
    public static void premain(String agentArgument, Instrumentation instrumentation){
             try {
                RpcProviderImpl.getInstance().initilize().register();

            }catch(Exception e){
                logger.fatal("jdry agent load fail ",e);
                System.exit(1);
            }
     }









}
