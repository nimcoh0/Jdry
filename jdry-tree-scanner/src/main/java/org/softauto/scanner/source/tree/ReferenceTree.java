package org.softauto.scanner.source.tree;


import java.util.HashMap;
import java.util.List;

public interface ReferenceTree extends Tree {

    List<HashMap<String,String>> getReferences();
}
