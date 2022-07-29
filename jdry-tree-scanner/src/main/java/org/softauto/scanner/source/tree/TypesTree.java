package org.softauto.scanner.source.tree;

import java.util.HashMap;
import java.util.List;

public interface TypesTree extends Tree {

    List<HashMap<String,Object>> getTypes();

    ParametersTree getParameter();

    ReturnTree getReturn();
}
