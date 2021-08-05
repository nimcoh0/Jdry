package org.softauto.jaxrs.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.core.AbstractMessage;
import org.softauto.jaxrs.annotations.JAXRS;

import javax.lang.model.element.Element;

/**
 * jaxrs Message Handler
 */
public class MessageHandler extends AbstractMessage {


       private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(MessageHandler.class);

       public JsonNode parser(Element element) {
            try {
                if (element.getAnnotation(JAXRS.class) != null) {
                   JsonNode jaxRSMethod  =  super.parseElement(element,new JaxRSMethodVisitor());
                   return jaxRSMethod;
                }
            }catch (Exception e){
                logger.error("fail parse jaxrs element ",e);
            }
           return null;
       }






}