package org.softauto.socket.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.annotations.ApiForTesting;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.core.AbstractMessage;
import org.softauto.core.vistors.DefaultMethodVistor;


import javax.lang.model.element.Element;

/**
 * parse method of type org.softauto.socket
 */
public class MessageHandler extends AbstractMessage {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MessageHandler.class);

    @Override
    public JsonNode parser(Element element) {
        try {
            if (element.getAnnotation(ExposedForTesting.class).protocol().equals("SOCKET") || element.getAnnotation(ApiForTesting.class).protocol().equals("SOCKET")) {
                return super.parseElement(element,new DefaultMethodVistor("SOCKET"));
            }
        }catch (Exception e){
            logger.error("fail parse SOCKET element ",e);
        }
        return null;
    }

}
