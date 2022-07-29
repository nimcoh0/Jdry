package org.softauto.scanner.source.tree;



import java.util.HashMap;

public interface ExposedAnnotationTree extends Tree {


    HashMap<String,Object> getAnnotations();

}
