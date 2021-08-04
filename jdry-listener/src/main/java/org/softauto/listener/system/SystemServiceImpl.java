package org.softauto.listener.system;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * System service impl
 */
public class SystemServiceImpl implements SystemService{

    private static SystemServiceImpl systemServiceImpl = null;


    @Override
    public int org_softauto_grpc_system_SystemServiceImpl_hello() {
        return 0;
    }

    @Override
    public int org_softauto_grpc_system_SystemServiceImpl_keepAlive() {
        return 0;
    }

    @Override
    public int org_softauto_grpc_system_SystemServiceImpl_configuration(JsonNode configuration) {
        return 0;
    }

    @Override
    public void org_softauto_grpc_system_SystemServiceImpl_shutdown() {

    }

    @Override
    public void org_softauto_grpc_system_SystemServiceImpl_acknowledge() {

    }

    @Override
    public void org_softauto_grpc_system_SystemServiceImpl_startTest(String testname) {

    }

    @Override
    public void org_softauto_grpc_system_SystemServiceImpl_endTest(String testname) {

    }
}
