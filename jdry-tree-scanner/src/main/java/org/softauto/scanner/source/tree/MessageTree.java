package org.softauto.scanner.source.tree;




import java.util.HashMap;

public interface MessageTree extends Tree {

    NamespaceTree getNamespace();

    TypesTree getTypesTree();

    MethodTree getMethod();

    ClassTree getClazz();

    RequestTree getRequest();

    ResponseTree getResponse();

    HashMap<String, Object> getMessage();

    //List<HashMap<String,Object>> getTypes();

    //TypeTree getType();

    AnnotationTree getAnnotations();

    HashMap<String, Object> buildMessage();

    //HashMap<String ,Object> getSchema();

    //ReferenceTree getReference();

}
