package org.softauto.scanner.source.tree;

import org.apache.avro.Suite;

import java.util.List;

public interface StepTree extends Tree {

    List<Suite.Step> getSteps();




}
