package org.softauto.core;

import java.util.HashMap;

public class Context {

    static HashMap<String,Object> ctx = new HashMap();

    public static void add(String key,Object value){
        ctx.put(key,value);
    }

    public static Object get(String key){
        return ctx.get(key);
    }

    public final static String TEMP_DIRECTORY = "temp_directory";
    public final static String LIB_HEAP_HELPER_NAME = "lib_heap_helper_name";
    public final static String TEST_MACHINE = "test_machine";
    public final static String LISTENER_PORT = "listener_port";
    public final static String TEST_MACHINE_NAME = "test_machine_name";
    public final static String TEST_INFRASTRUCTURE_PATH = "tests_infrastructure_path";
    public final static String STEP_SERVICE =  "StepService";
    public final static String LISTENER_SERVICE_IMPL = "ListenerServiceImpl";
    public final static String LISTENER_SERVICE_LOG_IMPL = "ListenerServiceLogImpl";
    public final static String LISTENER_SERVICE = "ListenerService";
    public final static String LISTENER_SERVICE_LOG = "ListenerServiceLog";
    public final static String SERIALIZER_PORT = "serializer_port";
    public final static String SERIALIZER_HOST = "serializer_host";
    //public final static String SYSTEM_PORT = "system_port";
    public final static String GUICE_MODULE = "guice_module";
    public final static String LISTENER_MODULE = "listener_module";
    public final static String ASPECT_WEAVER = "aspectjweaver";
    public final static String M2_ROOT = "m2_root";
}
