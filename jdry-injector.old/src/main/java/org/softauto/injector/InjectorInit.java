package org.softauto.injector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.ServiceLocator;

import java.io.IOException;


/**
 * initilize the injector
 */
public class InjectorInit {

    Injector injector = null;
    private static final Logger logger = LogManager.getLogger(InjectorInit.class);
    private static InjectorInit injectorProviderImpl = null;
    String name;
    public ObjectMapper objectMapper = null;


    public static InjectorInit getInstance(){
        if(injectorProviderImpl == null){
            injectorProviderImpl =  new InjectorInit();
        }
        return injectorProviderImpl;
    }

   private InjectorInit(){
       objectMapper = new ObjectMapper();
   }




    public InjectorInit initilize() throws IOException {
        try {
            injector = new Injector().createServiceDefinition();
            logger.debug("successfully initilize");
        }catch (Throwable e){
            logger.fatal("fail to load injector ", e);
            System.exit(1);
        }finally {

        }
        return this;
    }


    public void register() {
        ServiceLocator.getInstance().register("INJECTOR",injector);
    }

    public InjectorInit setName(String name){
        this.name = name;
        return this;
    }


}
