package org.softauto.discovery;

import com.fasterxml.jackson.databind.JsonNode;


public abstract class AbstractSchema {

    public JsonNode node ;
    public abstract void accept(Visitor v);
}
