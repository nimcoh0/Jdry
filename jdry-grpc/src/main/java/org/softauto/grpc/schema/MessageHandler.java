package org.softauto.grpc.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;
import org.softauto.core.AbstractMessage;
import org.softauto.core.MargeJsonNode;
import org.softauto.core.vistors.DefaultMethodVistor;

import javax.lang.model.element.Element;

/**
 * Handler for methods annotated with @RPC
 * use in the build annotation process to create the schema files
 */
public class MessageHandler extends AbstractMessage {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(MessageHandler.class);
       Element element;

    @Override
    public JsonNode parser(Element element) {
        try {
            if (element.getAnnotation(RPC.class) != null) {
                if(element.getAnnotation(ListenerForTesting.class) != null) {
                    JsonNode  listenerResult =  super.parseElement(element, new ListenerResultVistor("RPC"));
                    JsonNode  listenerData = super.parseElement(element, new ListenerDataVistor("RPC"));
                    return new MargeJsonNode().mergeNode((ObjectNode)listenerResult,(ObjectNode)listenerData);
                }else {
                    return super.parseElement(element, new DefaultMethodVistor("RPC"));
                }
            }
        }catch (Exception e){
            logger.error("fail parse RPC element ",e);
        }
        return null;
    }




}
