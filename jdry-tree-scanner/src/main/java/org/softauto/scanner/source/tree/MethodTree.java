package org.softauto.scanner.source.tree;

public interface MethodTree extends Tree {

    AnnotationTree getAnnotations();

    ModifiersTree getModifiers();

    String getName();

    ReturnTree getReturn();

    Object[] args();

    ParametersTree getParameter();

    ClassTree getClazz();





    
}
