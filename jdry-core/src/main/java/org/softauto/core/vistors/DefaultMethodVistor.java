package org.softauto.core.vistors;

import com.sun.tools.javac.code.Symbol;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.softauto.core.AbstractMessage;
import org.softauto.core.ClassTypeAnalyzer;
import org.softauto.core.vistors.builders.ClazzBuilder;
import org.softauto.core.vistors.builders.MessageBuilder;
import org.softauto.core.vistors.builders.TypeBuilder;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static javax.lang.model.element.Modifier.STATIC;

/**
 * default method parser
 */
public  class DefaultMethodVistor implements ElementVisitor {

    protected String key;
    protected HashMap<String,HashMap<String,Object>> messages ;
    protected Element element;
    ClazzBuilder.Builder clazzBuilder;
    MessageBuilder.Builder messageBuilder;
    TypeBuilder.Builder typeBuilder;

    public DefaultMethodVistor(String protocol){
        messages = new HashMap<>();
        clazzBuilder = ClazzBuilder.newBuilder();
        messageBuilder = MessageBuilder.newBuilder().setTransceiver(protocol);
        typeBuilder = TypeBuilder.newBuilder();
    }

    public ClazzBuilder.Builder getClazzBuilder() {
        return clazzBuilder;
    }

    public MessageBuilder.Builder getMessageBuilder() {
        return messageBuilder;
    }

    public TypeBuilder.Builder getTypeBuilder() {
        return typeBuilder;
    }

    public HashMap<String, Object> getNode(){
        HashMap<String, Object> o = new HashMap<>();
        messageBuilder.setClazz(clazzBuilder.build());
        messages.put(key,messageBuilder.build());
        o.put("types", typeBuilder.build());
        o.put("messages", messages);
        return o;
    }

    @Override
    public Object visit(Element e, Object o) {
        return null;
    }

    @Override
    public Object visit(Element e) {
        return null;
    }

    @Override
    public Object visitPackage(PackageElement e, Object o) {
        return null;
    }

    @Override
    public Object visitType(TypeElement e, Object o) {
        return null;
    }

    @Override
    public Object visitVariable(VariableElement e, Object o) {
        if (!AbstractMessage.isIgnore(e)) {
            addRequest(e);

            if(((Symbol)e).asType().allparams().size() > 0){
                if(((Symbol)e).asType().allparams().get(0).tsym.getKind().equals(ElementKind.TYPE_PARAMETER)){
                    typeBuilder.addType(e.asType().toString(),"generic");
                }else {
                    typeBuilder.addType(e.asType().toString(),"external");
                }
            }else
            if (e.asType().getKind().equals(TypeKind.TYPEVAR) ) {
                typeBuilder.addType(e.asType().toString(),"generic");
            } else {
                typeBuilder.addType(e.asType().toString(),"external");
            }
        }
        return o;
    }




    @Override
    public Object visitExecutable(ExecutableElement e, Object o) {
        element = e;
        String className = ElementUtils.getName(e.getEnclosingElement().asType());
        messageBuilder.setNamespace(e.getEnclosingElement().toString()).setMethod(e.getSimpleName().toString());
        clazzBuilder.setFullClassName(className);
        Set<Modifier> modifiers = e.getModifiers();
        clazzBuilder.setInitialize(new ClassTypeAnalyzer(ElementUtils.getClass(element.getEnclosingElement())).getClassType());
        if(modifiers.contains(STATIC)){
            messageBuilder.setType("static");
        }else

        if(ElementUtils.isConstructor(e)){
            messageBuilder.setType("constructor").setMethod(className.substring(className.lastIndexOf(".")+1));
        }else {
            messageBuilder.setType("method");
        }

        visitMessageKey(e);
        visitReturnType(e);
        clazzBuilder.setConstructorRequest(getConstructorDefaultValues(e.getEnclosingElement()));
        for (int i = 0; i <  e.getParameters().size(); i++) {
            VariableElement param = e.getParameters().get(i);
            visitVariable(param,o);
        }
        return getNode();
    }


    private List<HashMap<String,Object>> getConstructorDefaultValues(Element element){
        List<HashMap<String,Object>> defaultValues = new ArrayList<>();
        JavaClassSource javaClass = ElementUtils.getClass(element);
        List<MethodSource<JavaClassSource>> constructors = ElementUtils.getConstructors(javaClass);
        for(MethodSource<JavaClassSource> constructor : constructors) {
            defaultValues = ElementUtils.getConstructorDefaultValues(constructor);
        }
        return defaultValues;
    }


    @Override
    public Object visitTypeParameter(TypeParameterElement e, Object o) {
        return null;
    }

    @Override
    public Object visitUnknown(Element e, Object o) {
        return null;
    }


    public void visitReturnType(ExecutableElement e) {
        if(ElementUtils.isConstructor(e)){
            messageBuilder.setResponse(clazzBuilder.getFullClassName());
        }else {
            messageBuilder.setResponse(e.getReturnType().toString());
        }

        if (e.getReturnType().getKind().equals(TypeKind.TYPEVAR) ) {
            typeBuilder.addType(e.getReturnType().toString(),"generic");
        } else {
            typeBuilder.addType(e.getReturnType().toString(),"external");
        }
     }


    public void visitMessageKey(ExecutableElement e) {
        key = e.getEnclosingElement().toString() + "." + messageBuilder.getMethod();
        key = key.replace(".", "_").trim();
    }

    public void addRequest(VariableElement e){
            if(ElementUtils.hasDefault(e)){
                messageBuilder.addRequest(e.getSimpleName().toString(),e.asType().toString(),ElementUtils.getDefault(e));
            }else {
                messageBuilder.addRequest(e.getSimpleName().toString(), e.asType().toString(), null);
            }

    }

}
