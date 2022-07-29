package org.softauto.scanner.source.tree;

import java.util.HashMap;
import java.util.List;

public interface ParametersTree extends Tree {

   List<HashMap<String,Object>> getParameters();

   VariableTree getVariable();
}
