package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static Entity buildEntity(String produces, Object[] args){
        Entity<?> entity = null;
        try{
               if (produces != null && produces.equals(MediaType.MULTIPART_FORM_DATA_TYPE)) {
                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = new ObjectMapper().writeValueAsString(args[i]);
                        form.param(args[i].getClass().getName(), json);
                    }

                    entity = Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE);
                }
                if (produces != null && produces.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = new ObjectMapper().writeValueAsString(args[i]);
                        form.param(args[i].getClass().getName(), json);
                    }
                    entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
                }
                if (produces != null &&  produces.equals(MediaType.APPLICATION_JSON)) {
                    entity = Entity.entity(args[0],MediaType.APPLICATION_JSON);
                }
         }catch (Exception e){
            e.printStackTrace();
        }
        return entity;
    }


}
