package org.softauto.scanner.source.tree;

import org.apache.avro.Protocol;
import org.apache.avro.Suite;


public interface ApiTree extends Tree {

    Suite.Api getApi();


    Protocol.Message getMessage();


}
