package org.softauto.discovery.schema;

import org.softauto.discovery.AbstractSchema;
import org.softauto.discovery.Visitor;

public class SchemaHeaderHandler extends AbstractSchema {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SchemaHeaderHandler.class);
    private static String SCHEMA_VERSION = "1.0";
    private static String SCHEMA_NAMESPACE = "tests.infrastructure";
    static final private String STEP_PROTOCOL_NAME = "StepService";
    static final private String LISTENER_PROTOCOL_NAME = "ListenerService";

    private String protocol ;

    private String version = SCHEMA_VERSION;

    private String namespace =SCHEMA_NAMESPACE;



    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

        public String getProtocol() {
            return protocol;
        }

        public SchemaHeaderHandler setProtocol(String protocol) {
            this.protocol = protocol;
            return  this;
        }

        public String getVersion() {
            return version;
        }

        public SchemaHeaderHandler setVersion(String version) {
            this.version = version;
            return  this;
        }

        public String getNamespace() {
            return namespace;
        }

        public SchemaHeaderHandler setNamespace(String namespace) {
            this.namespace = namespace;
            return  this;
        }


}
