package org.softauto.local.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.core.AbstractMessage;
import org.softauto.local.annotations.LOCAL;

import javax.lang.model.element.Element;

/**
 * local Message Handler
 */
public class MessageHandler extends AbstractMessage {


       private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MessageHandler.class);

       public JsonNode parser(Element element) {
            try {
                if (element.getAnnotation(LOCAL.class) != null) {
                   JsonNode localMethod  =  super.parseElement(element,new LocalMethodVisitor());
                   return localMethod;
                }
            }catch (Exception e){
                logger.error("fail parse local element ",e);
            }
           return null;
       }






}
