package org.softauto.scanner.source.tree;



import java.util.HashMap;

public interface ListenerAnnotationTree extends Tree {

    HashMap<String,Object> getAnnotations();
}
