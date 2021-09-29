package org.softauto.logger;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.plugin.api.Provider;

import javax.lang.model.element.Element;
import java.io.IOException;

public class LoggerProviderImpl implements Provider {

    private static LoggerProviderImpl loggerProviderImpl = null;

    public static LoggerProviderImpl getInstance(){
        if(loggerProviderImpl == null){
            loggerProviderImpl = new LoggerProviderImpl();
        }
        return loggerProviderImpl;
    }

    private LoggerProviderImpl(){

    }

    @Override
    public Provider initilize() throws IOException {
        return this;
    }

    @Override
    public void register() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return "LOGGER";
    }

    @Override
    public JsonNode parser(Element element) {
        return null;
    }

    @Override
    public Provider iface(Class iface) {
        return this;
    }

    @Override
    public <RespT> void exec(String methodName,  org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel,Object...args) {

    }
}
