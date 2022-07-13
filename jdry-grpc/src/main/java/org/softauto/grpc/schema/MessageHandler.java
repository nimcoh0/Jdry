package org.softauto.grpc.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.annotations.ApiForTesting;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.core.AbstractMessage;
import org.softauto.core.vistors.DefaultMethodVistor;

import javax.lang.model.element.Element;

/**
 * Handler for methods annotated with @RPC
 * use in the build annotation process to create the schema files
 */
public class MessageHandler extends AbstractMessage {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MessageHandler.class);


    @Override
    public JsonNode parser(Element element) {
        try {
               if(element.getAnnotation(ListenerForTesting.class) != null) {
                    JsonNode  listenerData = super.parseElement(element, new ListenerDataVistor("RPC"));
                    return listenerData;
                }else {
                    if (element.getAnnotation(ExposedForTesting.class).protocol().equals("RPC") || element.getAnnotation(ApiForTesting.class).protocol().equals("RPC")) {
                        return super.parseElement(element, new DefaultMethodVistor("RPC"));
                    }
                }
        }catch (Exception e){
            logger.error("fail parse RPC element ",e);
        }
        return null;
    }




}
