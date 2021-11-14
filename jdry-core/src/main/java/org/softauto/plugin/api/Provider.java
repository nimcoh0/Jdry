package org.softauto.plugin.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;


import javax.lang.model.element.Element;
import java.io.IOException;

public interface Provider {

     /**
      * initilize plugin
      * @return
      * @throws IOException
      */
     Provider initialize()throws IOException;

     /**
      * register plugin in the ServiceLocator
      */
     void register();

     /**
      * shutdown plugin
      */
     void shutdown();


     /**
      * get this plugin type
      * @return
      */
     String getType();

     /**
      * parse new element of this provider type
      * @param element
      * @return
      */
     JsonNode parser(Element element);

     /**
      * set schema interface class
      * @param iface
      * @return
      */
     Provider iface(Class iface);

     /**
      * execute this plugin protocol request
      * @param methodName
      * @param args
      * @param callback
      * @param <RespT>
      */
     <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel,Object...args);

}
