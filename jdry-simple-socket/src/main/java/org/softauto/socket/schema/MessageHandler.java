package org.softauto.socket.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.core.AbstractMessage;
import org.softauto.core.vistors.DefaultMethodVistor;
import org.softauto.socket.annotations.SOCKET;

import javax.lang.model.element.Element;

/**
 * parse method of type org.softauto.socket
 */
public class MessageHandler extends AbstractMessage {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MessageHandler.class);

    @Override
    public JsonNode parser(Element element) {
        try {
            if (element.getAnnotation(SOCKET.class) != null) {
                return super.parseElement(element,new DefaultMethodVistor("SOCKET"));
            }
        }catch (Exception e){
            logger.error("fail parse SOCKET element ",e);
        }
        return null;
    }

}
