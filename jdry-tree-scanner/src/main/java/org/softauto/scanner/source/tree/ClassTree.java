package org.softauto.scanner.source.tree;


import java.util.HashMap;

public interface ClassTree extends Tree {

    ModifiersTree getModifiers();

    //AnnotationTree getAnnotations();

    HashMap<String,Object> getProperties(AnnotationTree annotationTree);
    /**
     * Returns the simple name of this type declaration.
     * @return the simple name
     */
    String getName();


}
