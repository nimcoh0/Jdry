package org.softauto.scanner.source.tree;

import org.apache.avro.Suite;


public interface TestTree extends Tree {

    Suite.Test getTest();

    //StepTree getStepTree();

    //VerifyTree getVerifyTree();

    //ListenerTree getListenerTree();

    //TypesTree getTypesTree();

    ProtocolTree getProtocolTree();

    //DependenceTree getDependenceTree();

    DataTree getDataTree();


}
