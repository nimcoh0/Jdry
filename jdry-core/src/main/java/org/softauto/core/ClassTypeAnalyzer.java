package org.softauto.core;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.softauto.core.vistors.ElementUtils;

import java.util.List;

/**
 * Analyze class load type
 */
public class ClassTypeAnalyzer {

    JavaClassSource javaClass;

    public ClassTypeAnalyzer(JavaClassSource javaClass){
        this.javaClass = javaClass;
    }


    public boolean isPublic(){
        if(javaClass.isPublic() && !javaClass.isAbstract() ){
            return true;
        }
        return false;
    }

    public boolean isInitializeWithParams(){
        List<MethodSource<JavaClassSource>> constructors = ElementUtils.getConstructors(javaClass);
        for(MethodSource<JavaClassSource> constructor : constructors) {
            if(constructor.isPrivate() || constructor.isProtected()){
                return false;
            }
            if(constructor.isPublic() && constructor.getParameters().size() == 0){
                return false;
            }
        }
        if(constructors.size() == 0){
            return false;
        }
        return true;
    }

    public boolean isInitializeWithNoParams(){
        List<MethodSource<JavaClassSource>> constructors = ElementUtils.getConstructors(javaClass);
        for(MethodSource<JavaClassSource> constructor : constructors) {
            if(constructor.isPublic() && constructor.getParameters().size() == 0){
                return true;
            }
        }
        if(constructors.size() == 0){
            return true;
        }
        return false;
    }

    public boolean isSingleton(){
        List<MethodSource<JavaClassSource>> methods = javaClass.getMethods();
        List<MethodSource<JavaClassSource>> constructors = ElementUtils.getConstructors(javaClass);
        for(MethodSource<JavaClassSource> constructor : constructors) {
            if(constructor.isPrivate()){
                for(MethodSource<JavaClassSource> m : methods){
                    if(m.getName().equals("getInstance")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ClassType getClassType(){
        if(!this.isPublic()){
            return ClassType.NONE;
        }
        if(this.isSingleton()){
            return ClassType.SINGLETON;
        }
        if(this.isInitializeWithNoParams()){
            return ClassType.INITIALIZE_NO_PARAM;
        }
        if(this.isInitializeWithParams()){
            return ClassType.INITIALIZE;
        }
        return ClassType.NONE;
    }

}
