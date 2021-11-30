package org.softauto.core;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;

public class Context {

    //static HashMap<String,Object> ctx = new HashMap();
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Context.class);
    //public static void add(String key,Object value){
    //    ctx.put(key,value);
   // }

    //public static Object get(String key){
     //   return ctx.get(key);
   // }
    public final static String ENABLE_SESSION = "enable_session";
    public final static String SEND_LOG_TO_TESTER = "send_log_to_tester";
    public final static String SEND_JDRY_LOG_TO_TESTER = "send_jdry_log_to_tester";
    public final static String LOAD_WEAVER = "load_weaver";
    //public final static String TEMP_DIRECTORY = "temp_directory";
    //public final static String LIB_HEAP_HELPER_NAME = "lib_heap_helper_name";
    public final static String TEST_MACHINE = "test_machine";
    public final static String LISTENER_PORT = "listener_port";
    public final static String TEST_MACHINE_NAME = "test_machine_name";
    public final static String TEST_INFRASTRUCTURE_PATH = "tests_infrastructure_path";
    public final static String STEP_SERVICE_NAME =  "step_service_name";
    public final static String STEP_SERVICE_IMPL =  "step_service_impl";
    public final static String STEP_SERVICE_IMPL_FOR_PROXY =  "step_service_impl_for_proxy";
    public final static String LISTENER_SERVICE_NAME = "listener_service_name";
    public final static String LISTENER_SERVICE_IMPL = "listener_service_impl";
    public final static String LISTENER_SERVICE_IMPL_FOR_PROXY = "listener_service_impl_for_proxy";
    //public final static String LISTENER_SERVICE_LOG_IMPL = "ListenerServiceLogImpl";
    //public final static String LISTENER_SERVICE_LOG = "ListenerServiceLog";
    public final static String SERIALIZER_PORT = "serializer_port";
    public final static String SERIALIZER_HOST = "serializer_host";
    //public final static String SYSTEM_PORT = "system_port";
    //public final static String GUICE_MODULE = "guice_module";
    //public final static String LISTENER_MODULE = "listener_module";
    public final static String ASPECT_WEAVER = "aspectj_weaver";
    //public final static String M2_ROOT = "m2_root";
    //public final static Marker TRACER = MarkerManager.getMarker("TRACER");
    //public final static Marker JDRY = MarkerManager.getMarker("JDRY");
    public static final String DEFUALT_DATETIME_FORMAT = "default_datatime_format";
    //public final static String LOG4J2_SOCKET_TCP_SERVER_PORT = "log4j2_socket_tcp_server_port";
    public static TestLifeCycle TestState = TestLifeCycle.NONE;

    public static TestLifeCycle getTestState() {
        return TestState;
    }

    public static void setTestState(TestLifeCycle testState) {
        TestState = testState;
        logger.debug("test state change to "+ testState);
    }
}
