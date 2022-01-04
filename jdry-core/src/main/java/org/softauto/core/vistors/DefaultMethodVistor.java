package org.softauto.core.vistors;

import org.softauto.core.Utils;
import org.softauto.core.vistors.builders.ClazzBuilder;
import org.softauto.core.vistors.builders.MessageBuilder;
import org.softauto.core.vistors.builders.TypeBuilder;
import javax.lang.model.element.*;
import java.util.HashMap;
import java.util.Set;

import static javax.lang.model.element.Modifier.STATIC;

/**
 * default method parser
 */
public  class DefaultMethodVistor implements ElementVisitor {

    protected String key;
    protected HashMap<String,HashMap<String,Object>> messages ;
    ClazzBuilder.Builder clazzBuilder;
    MessageBuilder.Builder messageBuilder;
    TypeBuilder.Builder typeBuilder;


    public DefaultMethodVistor(String protocol){
        messages = new HashMap<>();
        clazzBuilder = ClazzBuilder.newBuilder();
        messageBuilder = MessageBuilder.newBuilder().setTransceiver(protocol);
        typeBuilder = TypeBuilder.newBuilder();
    }

    public HashMap<String, Object> getNode(){
        HashMap<String, Object> o = new HashMap<>();
        messageBuilder.setClazz(clazzBuilder.build());
        messages.put(key,messageBuilder.build());
        o.put("types", typeBuilder.build());
        o.put("messages", messages);
        return o;
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
        try {
            if (e.getKind().equals(ElementKind.FIELD)) {
                String className = e.getEnclosingElement().toString();
                messageBuilder.setNamespace(e.getEnclosingElement().toString());
                messageBuilder.setMethod(e.getSimpleName().toString());
                clazzBuilder.setFullClassName(className);
                clazzBuilder.setInitialize(new ClassTypeAnalyzer(e).getClassType());
                messageBuilder.setType("variable");
                key = e.getEnclosingElement().toString() + "." + messageBuilder.getMethod();
                key = key.replace(".", "_").trim();
                messageBuilder.setResponse("void");
                if(!Utils.isSchemaType(e.asType().toString()))
                    typeBuilder.addType(e.asType().toString(),"external");
                messageBuilder.addRequest(e.getSimpleName().toString(), Utils.getSchemaType(e.asType().toString()), null);

                return getNode();
            } else {

                if (ElementUtils.hasDefault(e)) {
                    messageBuilder.addRequest(e.getSimpleName().toString(), Utils.getSchemaType(e.asType().toString()), ElementUtils.getDefault(e));
                    typeBuilder.addType(e.asType().toString(), "external");
                } else {
                    messageBuilder.addRequest(e.getSimpleName().toString(), Utils.getSchemaType(e.asType().toString()), null);
                    typeBuilder.addType(e.asType().toString(), "external");
                }
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
        return o;
    }




    @Override
    public Object visitExecutable(ExecutableElement e, Object o) {
        try {
            String className = e.getEnclosingElement().toString();
            messageBuilder.setNamespace(e.getEnclosingElement().toString())
                          .setMethod(e.getSimpleName().toString());
            clazzBuilder.setFullClassName(className);
            Set<Modifier> modifiers = e.getModifiers();
            clazzBuilder.setInitialize(new ClassTypeAnalyzer(e).getClassType());
            if (modifiers.contains(STATIC)) {
                messageBuilder.setType("static");
            } else if (ElementUtils.isConstructor(e)) {
                messageBuilder.setType("constructor").setMethod(className.substring(className.lastIndexOf(".")+1));;
            } else {
                messageBuilder.setType("method");
            }

            key = e.getEnclosingElement().toString() + "." + messageBuilder.getMethod();
            key = key.replace(".", "_").trim();
            if(ElementUtils.isConstructor(e)){
                messageBuilder.setResponse(clazzBuilder.getFullClassName());
                typeBuilder.addType(clazzBuilder.getFullClassName(),"external");
            }else {
                messageBuilder.setResponse(Utils.getSchemaType(e.getReturnType().toString()));
                if (!Utils.isSchemaType(e.getReturnType().toString()))
                    typeBuilder.addType(e.getReturnType().toString(), "external");
            }
            for (int i = 0; i < e.getParameters().size(); i++) {
                VariableElement param = e.getParameters().get(i);
                visitVariable(param, o);
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
        return getNode();
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
            typeBuilder.addType(clazzBuilder.getFullClassName(),"external");
        }else {
            messageBuilder.setResponse(Utils.getSchemaType(e.getReturnType().toString()));
            if (!Utils.isSchemaType(e.getReturnType().toString()))
                typeBuilder.addType(e.getReturnType().toString(), "external");
        }
     }



}
