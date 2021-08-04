package org.softauto.core.vistors;


import com.sun.tools.javac.code.Symbol;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.ValuePair;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.softauto.annotations.DefaultValue;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * helper for elements
 */
public class ElementUtils {

    /**
     * get element class name
     * @param element TypeElement
     * @return
     */
    public static String getName(TypeElement element) {
        try {
            String name = getClassName(element);
            return name;
         } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * get element class
     * @param element TypeElement
     * @return
     */
    public static Class getClass(TypeElement element) {
        try {
            return Class.forName(getClassName(element));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * get element name
     * @param type TypeMirror
     * @return
     */
    public static String getName(TypeMirror type) {
        if (type instanceof DeclaredType) {
            if (((DeclaredType) type).asElement() instanceof TypeElement) {
                return getName((TypeElement) ((DeclaredType) type)
                        .asElement());
            }
        }
        return null;
    }


    /**
     * get element class using class source
     * @param element
     * @return
     */
    public static JavaClassSource getClass(Element element){
        try {
            Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol) element;
            URI uri = classSymbol.sourcefile.toUri();
            return Roaster.parse(JavaClassSource.class, new File(uri.getPath()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * get class
     * @param type TypeMirror
     * @return
     */
    public static Class getClass(TypeMirror type){
        try {
            if (type instanceof DeclaredType) {
                if (((DeclaredType) type).asElement() instanceof TypeElement) {
                    return getClass((TypeElement) ((DeclaredType) type)
                            .asElement());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get class Constructors list
     * @param c
     * @return
     */
    public static List<Constructor> getConstructors(Class c){
        return Arrays.asList(c.getConstructors());
    }


    /**
     * get class Constructors list using class source
     * @param javaClass
     * @return
     */
    public static List<MethodSource<JavaClassSource>> getConstructors(JavaClassSource javaClass){
        List<MethodSource<JavaClassSource>> constructors = new ArrayList<>();
        List<MethodSource<JavaClassSource>> methodSource = javaClass.getMethods();
        for(MethodSource<JavaClassSource> ms : methodSource){
            if(ms.isConstructor()){
                constructors.add(ms) ;
            }
        }
        return constructors;
    }


    /**
     * get class methods using class source
     * @param javaClass
     * @return
     */
    public static List<MethodSource<JavaClassSource>> getMethods(JavaClassSource javaClass){
        return javaClass.getMethods();
    }


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
     * verify if element has this annotation
     * @param e
     * @param annotation
     * @return
     */
    public  static  boolean hasAnnotation(Element e, Class annotation){
        if(e.getAnnotation(annotation)!= null){
            return true;
        }
        return false;
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

    /**
     * get parameter default value
     * @param p
     * @return
     */
    public static String getDefaultValue(VariableElement p){
           if(p.getAnnotation(DefaultValue.class) != null){
              return  p.getAnnotation(DefaultValue.class).value();
            }
       return null;
    }

    /**
     * get parameter default type
     * @param p
     * @return
     */
    public static String getDefaultType(VariableElement p){
        if(p.getAnnotation(DefaultValue.class) != null){
            return  p.getAnnotation(DefaultValue.class).type();
        }
       return null;
    }

    /**
     * get Constructor parameters default values
     * using class source
     * @param constructor
     * @return
     */
    public static List<HashMap<String,Object>> getConstructorDefaultValues(MethodSource<JavaClassSource> constructor) {
        List<HashMap<String,Object>> defaultValues = new ArrayList<>();
        List<ParameterSource<JavaClassSource>> ps = constructor.getParameters();
        for(ParameterSource<JavaClassSource> p : ps){
            if(p.getAnnotation(DefaultValue.class) != null){
                HashMap<String,Object> hm = new HashMap();
                String name = p.getName();
                List<ValuePair> values = p.getAnnotation(DefaultValue.class).getValues();
                hm.put("name",name);
                hm.put("value",values.get(0).getStringValue());
                if(values.size()> 1)
                    hm.put("type",values.get(1).getStringValue());
                defaultValues.add(hm);
            }
        }
        return defaultValues;
    }

    /**
     * get Constructor parameters default values
     * @param constructor
     * @return
     */
    public static List<HashMap<String,Object>> getConstructorDefaultValues(Constructor constructor) {
        List<HashMap<String,Object>> defaultValues = new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        for(Parameter p : parameters){
            if(p.getAnnotation(DefaultValue.class) != null){
                HashMap<String,Object> hm = new HashMap();
                String name = p.getName();
                String value = p.getAnnotation(DefaultValue.class).value();
                if(p.getAnnotation(DefaultValue.class).type() != null && !p.getAnnotation(DefaultValue.class).type().isEmpty()){
                    String type = p.getAnnotation(DefaultValue.class).type();
                }
                defaultValues.add(hm);
            }
        }
        return defaultValues;
    }


    /**
     * get class name
     * @param element TypeElement
     * @return
     */
    public static String getClassName(TypeElement element) {
        Element currElement = element;
        String result = element.getSimpleName().toString();
        while (currElement.getEnclosingElement() != null) {
            currElement = currElement.getEnclosingElement();
            if (currElement instanceof TypeElement) {
                result = currElement.getSimpleName() + "$" + result;
            } else if (currElement instanceof PackageElement) {
                if (!"".equals(currElement.getSimpleName())) {
                    result = ((PackageElement) currElement)
                            .getQualifiedName() + "." + result;
                }
            }
        }
        return result;
    }
}
