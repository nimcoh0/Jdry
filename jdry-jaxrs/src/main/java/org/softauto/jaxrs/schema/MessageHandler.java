package org.softauto.jaxrs.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.core.AbstractMessage;
import org.softauto.jaxrs.annotations.JAXRS;

import javax.lang.model.element.Element;

/**
 * jaxrs Message Handler
 */
public class MessageHandler extends AbstractMessage {


       private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MessageHandler.class);

       public JsonNode parser(Element element) {
            try {
                if (element.getAnnotation(ExposedForTesting.class).protocol().equals("JAXRS")) {
                   JsonNode jaxRSMethod  =  super.parseElement(element,new JaxRSMethodVisitor());
                   return jaxRSMethod;
                }
            }catch (Exception e){
                logger.error("fail parse jaxrs element ",e);
            }
           return null;
       }






}
