package org.softauto.discovery;


import org.softauto.discovery.schema.SchemaHeaderHandler;
import org.softauto.discovery.schema.SchemaMessageHandler;
import org.softauto.discovery.schema.SchemaTypesHandler;

public interface Visitor {

    void visit(SchemaHeaderHandler p);
    void visit(SchemaTypesHandler p);
    void visit(SchemaMessageHandler p);

}
