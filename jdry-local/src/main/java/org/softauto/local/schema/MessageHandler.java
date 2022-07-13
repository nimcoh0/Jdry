package org.softauto.local.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.annotations.ApiForTesting;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.core.AbstractMessage;

import javax.lang.model.element.Element;

/**
 * local Message Handler
 */
public class MessageHandler extends AbstractMessage {


       private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MessageHandler.class);

       public JsonNode parser(Element element) {
            try {
                if (element.getAnnotation(ExposedForTesting.class).protocol().equals("LOCAL") || element.getAnnotation(ApiForTesting.class).protocol().equals("LOCAL")) {
                   JsonNode localMethod  =  super.parseElement(element,new LocalMethodVisitor());
                   logger.debug("local parse successfully");
                   return localMethod;
                }
            }catch (Exception e){
                logger.error("fail parse local element ",e);
            }
           return null;
       }






}
