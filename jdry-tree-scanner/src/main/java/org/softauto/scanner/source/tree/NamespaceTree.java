package org.softauto.scanner.source.tree;

public interface NamespaceTree extends Tree {

    String getNamespace();

    ClassTree getClazz();
}
