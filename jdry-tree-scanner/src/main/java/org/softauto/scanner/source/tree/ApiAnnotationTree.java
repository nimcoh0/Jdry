package org.softauto.scanner.source.tree;

import java.util.HashMap;

public interface ApiAnnotationTree extends Tree {

    HashMap<String,Object> getAnnotations();
}
