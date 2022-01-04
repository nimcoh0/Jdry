package org.softauto.core.vistors;


import org.softauto.annotations.DefaultValue;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;


/**
 * helper for elements
 */
public class ElementUtils {








    /**
     * validate if element is Constructor
     * @param e
     * @return
     */
    public static boolean isConstructor(Element e){
        if(((ExecutableElement)e).getKind().equals(ElementKind.CONSTRUCTOR)){
            return true;
        }
        return false;
    }

    /**
     * get annotation default value
     * @param p
     * @return
     */
    public static HashMap<String,String> getDefault(VariableElement p){
        if(p.getAnnotation(DefaultValue.class) != null){
            HashMap<String,String> hm = new HashMap<>();
            hm.put("value",p.getAnnotation(DefaultValue.class).value());
            if(!p.getAnnotation(DefaultValue.class).type().isEmpty()) {
                hm.put("type", p.getAnnotation(DefaultValue.class).type());
            }
            return  hm;
        }
       return null;
    }


    /**
     * verify if element parameters have default valus
     * @param p
     * @return
     */
    public static boolean hasDefault(VariableElement p){
        if(p.getAnnotation(DefaultValue.class) != null){
            return true;
        }
        return false;
    }


}
