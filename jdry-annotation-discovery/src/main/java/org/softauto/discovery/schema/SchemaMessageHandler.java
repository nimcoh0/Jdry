package org.softauto.discovery.schema;

import org.softauto.discovery.AbstractSchema;
import org.softauto.discovery.Visitor;

public class SchemaMessageHandler extends AbstractSchema {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
