package org.softauto.scanner.source.tree;

import java.util.HashMap;
import java.util.List;

public interface RequestTree extends Tree {

    List<HashMap<String,Object>> getRequest();

    ParametersTree getParameter();

    Object[] getArgs();

    AnnotationTree getAnnotationTree();
}
