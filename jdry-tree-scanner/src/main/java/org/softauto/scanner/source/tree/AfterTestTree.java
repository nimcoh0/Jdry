package org.softauto.scanner.source.tree;

import java.util.HashMap;

public interface AfterTestTree extends Tree {

    HashMap<String,Object> getAnnotations();
}
