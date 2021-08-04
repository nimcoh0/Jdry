package org.softauto.grpc.schema;

import org.softauto.core.AbstractMessage;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * parser for methods annotated as Listener for data inspection
 */
public  class ListenerDataVistor implements ElementVisitor {

    String protocol;
    String key;
    HashMap<String,Object> message;
    HashMap<String,HashMap<String,Object>> messages ;
    List<HashMap<String,Object>> types;
    List<HashMap<String,Object>> request;
    String response;
    String method;
    String namespace;

    public ListenerDataVistor(String protocol){
        this.protocol = protocol;
        message = new HashMap<>();
        messages = new HashMap<>();
        types = new ArrayList<>();
        request = new ArrayList<>();
    }

    public HashMap<String, Object> getNode(){
        HashMap<String, Object> o = new HashMap<>();
        message.put("request", request);
        message.put("response", "java.lang.Object[]");
        message.put("namespace", namespace);
        message.put("method", method);
        types.add(getType("java.lang.Object[]","external"));
        o.put("types", types);
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
            if (e.asType().getKind().equals(TypeKind.TYPEVAR)) {
                types.add(getType(e.asType().toString(),"generic"));
            } else {
                types.add(getType(e.asType().toString(),"external"));
            }
        }
        return o;
    }

    @Override
    public Object visitExecutable(ExecutableElement e, Object o) {
        namespace = e.getEnclosingElement().toString();
        method = e.getSimpleName().toString();
        message.put("transceiver", protocol);
        visitMessageKey(e);
        visitReturnType(e);
        for (int i = 0; i <  e.getParameters().size(); i++) {
            VariableElement param = e.getParameters().get(i);
            visitVariable(param,o);
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
        message.put("response", e.getReturnType().toString());
        types.add(getType(e.getReturnType().toString(),"external"));
    }


    public void visitMessageKey(ExecutableElement e) {
        key = e.getEnclosingElement().toString() + "." + e.getSimpleName().toString();
        key = key.replace(".", "_").trim();
        messages.put(key,message);
    }

    public List<HashMap<String,Object>> addRequest(VariableElement e){
        HashMap<String,Object> req = new HashMap<>();
        req.put("name",e.getSimpleName().toString());
        req.put("type",e.asType().toString());
        request.add(req);
        return request;
    }

    public HashMap<String,Object> getType(String e,String t){
        HashMap<String,Object> type = new HashMap<>();
        type.put("name",e);
        type.put("type",t);
        return type;
    }



}
