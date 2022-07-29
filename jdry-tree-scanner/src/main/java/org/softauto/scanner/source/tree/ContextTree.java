package org.softauto.scanner.source.tree;

import org.apache.avro.Item;

import java.util.List;

public interface ContextTree extends Tree {


    List<Item> getItemAndtemplates();
}
