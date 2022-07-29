package org.softauto.scanner.source.tree;

import org.apache.avro.Suite;

public interface VerifyTree extends Tree {

    Suite.Verify getVerify();

}
