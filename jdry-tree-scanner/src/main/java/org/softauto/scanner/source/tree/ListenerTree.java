package org.softauto.scanner.source.tree;

import org.apache.avro.Suite;

import java.util.List;

public interface ListenerTree extends Tree {

    List<Suite.Listener> getListeners();


}
