package org.softauto.scanner.source.tree;

import org.apache.avro.Suite;

public interface AssertTree extends Tree {

    Suite.Assert getAssert();
}
