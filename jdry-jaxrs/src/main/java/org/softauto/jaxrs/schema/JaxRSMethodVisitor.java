package org.softauto.jaxrs.schema;

import org.softauto.core.vistors.DefaultMethodVistor;

import javax.lang.model.element.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * jaxrs method parser
 */
public class JaxRSMethodVisitor extends DefaultMethodVistor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JaxRSMethodVisitor.class);
    HashMap<String, Object> hm;
    List<String> contentList;


    public JaxRSMethodVisitor(){
        super("JAXRS");
        hm = new HashMap<>();
        contentList = new ArrayList<>();
    }

    @Override
    public HashMap<String, Object> getNode(){
        HashMap<String, Object> o =  super.getNode();
        try {
            hm.put("content",contentList.toArray());
            ((HashMap)((HashMap)o.get("messages")).get(key)).put("jaxrs",hm);
        }catch (Exception e){
            logger.error("fail get node ",e);
        }
        return o;
    }



    @Override
    public Object visitType(TypeElement e, Object o) {
        return null;
    }

    @Override
    public Object visitVariable(VariableElement e, Object o) {
        o = super.visitVariable(e, o);
        if(hm.get("HttpMethod").toString().equals("POST") || hm.get("HttpMethod").toString().equals("PUT")) {
              contentList.add(e.getSimpleName().toString());
        }

        return o;
    }

    @Override
    public Object visitExecutable(ExecutableElement e, Object o) {

        for(AnnotationMirror annotation : e.getEnclosingElement().getAnnotationMirrors()) {
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("Path")) {
                hm.put("Path", e.getEnclosingElement().getAnnotation(javax.ws.rs.Path.class).value());
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("Produces")) {
                hm.put("Produces", e.getEnclosingElement().getAnnotation(javax.ws.rs.Produces.class).value()[0]);
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("Consumes")) {
                hm.put("Consumes", e.getEnclosingElement().getAnnotation(javax.ws.rs.Consumes.class).value()[0]);
            }
        }
        for(AnnotationMirror annotation : e.getAnnotationMirrors()){

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("GET")) {
                hm.put("HttpMethod", "GET");
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("POST")) {
                hm.put("HttpMethod", "POST");
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("PUT")) {
                hm.put("HttpMethod", "PUT");
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("DELETE")) {
                hm.put("HttpMethod", "DELETE");
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("Path")) {
                String path = e.getAnnotation(javax.ws.rs.Path.class).value();
                String classRootPath = hm.get("Path").toString();
                if(classRootPath != null && !classRootPath.isEmpty()){
                    hm.put("Path", classRootPath+path );
                }else {
                    hm.put("Path", path );
                }
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("Produces")) {
                hm.put("Produces", e.getAnnotation(javax.ws.rs.Produces.class).value()[0]);
            }

            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals("Consumes")) {
                hm.put("Consumes", e.getAnnotation(javax.ws.rs.Consumes.class).value()[0]);
            }
        }
        super.visitExecutable(e,o);
        return getNode();
    }


    @Override
    public Object visitTypeParameter(TypeParameterElement e, Object o) {
        return null;
    }

    @Override
    public void visitReturnType(ExecutableElement e) {
        super.visitReturnType(e);
        if(e.getReturnType().toString().equals("void")){
            if(hm.get("Produces").toString().equals(MediaType.APPLICATION_JSON)){
                super.getMessageBuilder().setResponse("java.lang.String");
            }

        }
    }

}
