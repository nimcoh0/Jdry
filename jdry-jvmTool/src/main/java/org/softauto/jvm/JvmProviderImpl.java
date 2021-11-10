package org.softauto.jvm;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.tools.attach.VirtualMachine;
import io.grpc.ManagedChannel;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.ServiceLocator;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;

import javax.lang.model.element.Element;
import java.io.*;
import java.lang.management.ManagementFactory;

public class JvmProviderImpl implements Provider {

    private static final Logger logger = LogManager.getLogger(JvmProviderImpl.class);
    private static JvmProviderImpl jvmProviderImpl = null;
    String type = "JVM";

    public static JvmProviderImpl getInstance(){
        if(jvmProviderImpl == null){
            jvmProviderImpl =  new JvmProviderImpl();
        }
        return jvmProviderImpl;
    }


    @Override
    public Provider initilize() throws IOException {
        try {
            String path = System.getenv("temp");
            String name = "libHeapHelper.dll";
            loadLib(path,name);
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            VirtualMachine jvm = VirtualMachine.attach(pid);
            jvm.loadAgentPath(path+"/"+name, null);
            logger.info("HeapHelper agent attach to pid " + pid + " successfully");
        }catch (Exception e){
            logger.error("HeapHelper agent fail to load",e);
        }
        return this;
    }

    private void loadLib(String path, String name) {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(name);
        OutputStream outputStream = null;
        try {
            File fileOut = new File(path+"/"+name);
            outputStream = new FileOutputStream(fileOut);
            IOUtils.copy(input, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void register() {
        ServiceLocator.getInstance().register("JVM",this);
    }

    @Override
    public void shutdown() {
        HeapHelper.clean();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public JsonNode parser(Element element) {
        return null;
    }

    @Override
    public Provider iface(Class iface) {
        return null;
    }

    @Override
    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {

    }
}
