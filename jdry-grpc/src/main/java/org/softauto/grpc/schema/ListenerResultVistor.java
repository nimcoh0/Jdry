package org.softauto.grpc.schema;

//import com.sun.tools.javac.code.Type;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.core.AbstractMessage;
import org.softauto.core.Utils;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * parser for methods annotated as Listener for result inspection
 */
public  class ListenerResultVistor implements ElementVisitor {

    String protocol;
    String key;
    HashMap<String,Object> message;
    HashMap<String,HashMap<String,Object>> messages ;
    List<HashMap<String,Object>> types;
    List<HashMap<String,Object>> request;
    String response;
    String method;
    String namespace;

    public ListenerResultVistor(String protocol){
        this.protocol = protocol;
        message = new HashMap<>();
        messages = new HashMap<>();
        types = new ArrayList<>();
        request = new ArrayList<>();
    }

    public HashMap<String, Object> getNode(){
        HashMap<String, Object> o = new HashMap<>();
        if(response.equals("null") || response.equals("void")) {
            message.put("request", request);
        }else {
            HashMap<String, String> hm = new HashMap<>();
            List<HashMap<String, String>> request = new ArrayList<>();
            hm.put("name", "result");
            hm.put("type", response);
            request.add(hm);
            message.put("request", request);
        }
        message.put("response", "null");
        message.put("namespace", namespace);
        message.put("method", method + "_result");
        visitMessageKey();
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
                if(!Utils.isSchemaType(e.asType().toString()))
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
        if(e.getAnnotation(ListenerForTesting.class).description() !=null && !e.getAnnotation(ListenerForTesting.class).description().isEmpty()){
            message.put("description", e.getAnnotation(ListenerForTesting.class).description());
        }
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
        response =  Utils.getSchemaType(e.getReturnType().toString());
        if(!Utils.isSchemaType(e.getReturnType().toString()))
        types.add(getType(e.getReturnType().toString(),"external"));
    }


    public void visitMessageKey() {
        key = namespace + "." + method + "_result";
        key = key.replace(".", "_").trim();
        messages.put(key,message);
    }

    public List<HashMap<String,Object>> addRequest(VariableElement e){
        HashMap<String,Object> req = new HashMap<>();
        req.put("name",e.getSimpleName().toString());
        req.put("type",Utils.getSchemaType(e.asType().toString()));
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
