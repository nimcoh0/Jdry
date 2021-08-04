package org.softauto.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.VariableElement;
import java.util.HashMap;

/**
 * Abstract class form message build in the annotation process
 */
public abstract class AbstractMessage {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(AbstractMessage.class);
    public ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ignore annotations in the list at the annotation process run
     */
    public static  Class[] ignoreParamAnnotationList = new Class[]{javax.ws.rs.container.Suspended.class,javax.ws.rs.core.Context.class};

    public AbstractMessage(){
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    }



    public abstract JsonNode parser(Element element);

    public static boolean isIgnore(VariableElement param ){
        for(Class ignore : ignoreParamAnnotationList){
            if(param.getAnnotation(ignore) != null){
                return true;
            }
        }
        return false;
    }

    public JsonNode parseElement(Element element, ElementVisitor visitor) {
        HashMap<String, Object> node = null;
        try {
                node = (HashMap<String, Object>)element.accept(visitor,null);
                JsonNode rootNode = objectMapper.createObjectNode();
                ((ObjectNode)rootNode).setAll((ObjectNode)objectMapper.valueToTree(node));
                logger.debug("add new message " + node);
                return rootNode;

        }catch (Exception e){
            logger.error("fail parse element ",e);
        }
        return null;
    }



}
