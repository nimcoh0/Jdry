package org.softauto.system;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.avro.Protocol;

/**
 * interface for system methods
 */
public interface SystemService {

    public static final Protocol PROTOCOL = Protocol.parse("{\"protocol\":\"SystemService\",\"namespace\":\"org.softauto.system\",\"version\":\"1.0\",\"types\":[{\"name\" : \"int\",\"type\" : \"external\"  },{\"name\" : \"com.fasterxml.jackson.databind.JsonNode\",\"type\" : \"external\"  },{\"name\" : \"java.lang.String\",\"type\" : \"external\"  }],\"messages\":{\"org_softauto_system_SystemServiceImpl_hello\":{\"method\":\"hello\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[],\"response\":\"int\"},\"org_softauto_system_SystemServiceImpl_keepAlive\":{\"method\":\"keepAlive\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[],\"response\":\"int\"},\"org_softauto_system_SystemServiceImpl_configuration\":{\"method\":\"configuration\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[{\"name\" : \"configuration\",\"type\" : \"com.fasterxml.jackson.databind.JsonNode\" }],\"response\":\"int\"},\"org_softauto_system_SystemServiceImpl_shutdown\":{\"method\":\"shutdown\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[],\"response\":\"null\"},\"org_softauto_system_SystemServiceImpl_acknowledge\":{\"method\":\"acknowledge\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[{\"name\" : \"acknowledge\",\"type\" : \"int\" }],\"response\":\"null\"},\"org_softauto_system_SystemServiceImpl_startTest\":{\"method\":\"acknowledge\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[{\"name\" : \"testname\",\"type\" : \"java.lang.String\" }],\"response\":\"null\"},\"org_softauto_system_SystemServiceImpl_endTest\":{\"method\":\"acknowledge\",\"transceiver\":\"RPC\",\"namespace\":\"org.softauto.system.SystemService\",\"request\":[{\"name\" : \"testname\",\"type\" : \"java.lang.String\" }],\"response\":\"null\"}}}");

    int org_softauto_system_SystemServiceImpl_hello();
    int org_softauto_system_SystemServiceImpl_keepAlive();
    int org_softauto_system_SystemServiceImpl_configuration(JsonNode configuration);
    void org_softauto_system_SystemServiceImpl_shutdown();
    void org_softauto_system_SystemServiceImpl_acknowledge();
    void org_softauto_system_SystemServiceImpl_startTest(String testname);
    void org_softauto_system_SystemServiceImpl_endTest(String testname);

    public interface Callback extends org.softauto.system.SystemService {
        public static final Protocol PROTOCOL = org.softauto.system.SystemService.PROTOCOL;

        void org_softauto_system_SystemServiceImpl_hello(org.softauto.serializer.CallFuture<Void> callback);
        void org_softauto_system_SystemServiceImpl_shutdown(org.softauto.serializer.CallFuture<Void> callback);
        void org_softauto_system_SystemServiceImpl_configuration(JsonNode configuration,org.softauto.serializer.CallFuture<Void> callback);
        void org_softauto_system_SystemServiceImpl_keepAlive( org.softauto.serializer.CallFuture<Void> callback);
        void org_softauto_system_SystemServiceImpl_acknowledge(org.softauto.serializer.CallFuture<Void> callback);
        void org_softauto_system_SystemServiceImpl_startTest(String testname,org.softauto.serializer.CallFuture<Void> callback);
        void org_softauto_system_SystemServiceImpl_endTest(String testname,org.softauto.serializer.CallFuture<Void> callback);
    }
}