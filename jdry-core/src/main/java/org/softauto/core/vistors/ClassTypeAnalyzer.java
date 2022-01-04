package org.softauto.core.vistors;


import org.softauto.core.ClassType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Set;

/**
 * Analyze class load type
 */
public class ClassTypeAnalyzer {

    Element element;

    public ClassTypeAnalyzer(Element element){
        this.element = element;
    }

    public boolean isPublic(){
        Set<Modifier> modifiers =  element.getModifiers();
        if(modifiers.contains(Modifier.PUBLIC)){
            return true;
        }
        return false;
    }

    public boolean isPrivate(){
        Set<Modifier> modifiers =  element.getModifiers();
        if(modifiers.contains(Modifier.PRIVATE)){
            return true;
        }
        return false;
    }

    public boolean isAbstract(){
        Set<Modifier> modifiers =  element.getModifiers();
        if(modifiers.contains(Modifier.ABSTRACT)){
            return true;
        }
        return false;
    }

    public  boolean isConstructor(){
        if(element.getKind().equals(ElementKind.CONSTRUCTOR)){
            return true;
        }
        return false;
    }


    public boolean isSingleton() throws Exception {
        if(isConstructor() && isPrivate()){
            for(Element e :  element.getEnclosingElement().getEnclosedElements()){
                if(e.getKind().name().equals(ElementKind.METHOD) && e.getSimpleName().equals("getInstance")){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInitializeWithParams() {
        if(isConstructor() && isPublic()){
            if(((ExecutableElement)element).getParameters().size()>0){
                return true;
            }
        }
        return false;
    }

    public boolean isInitializeWithNoParams() {
        if(isConstructor() && isPublic()){
            if(element.getEnclosingElement().getKind().name().equals(ElementKind.PARAMETER)){
                return false;
            }
        }
        return true;
    }


    public ClassType getClassType()throws Exception{
       // if(!isPublic() || isAbstract()){
         //   return ClassType.NONE;
        //}
        if(isSingleton()){
            return ClassType.SINGLETON;
        }
        if(isInitializeWithParams()){
            return ClassType.INITIALIZE;
        }
        if(isInitializeWithNoParams()){
            return ClassType.INITIALIZE_NO_PARAM;
        }
        return ClassType.NONE;
    }



}
