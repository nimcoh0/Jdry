package org.softauto.scanner.source.tree;

import org.apache.avro.Suite;

public interface DataTree extends Tree {

    Suite.Data getData();
}
