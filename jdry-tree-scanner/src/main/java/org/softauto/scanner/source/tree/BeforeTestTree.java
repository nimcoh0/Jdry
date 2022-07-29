package org.softauto.scanner.source.tree;

import java.util.HashMap;

public interface BeforeTestTree extends Tree {

    AuthenticationTree getAuthenticationTree();

    HashMap<String, HashMap<String, Object>> getBeforeTest();


}
