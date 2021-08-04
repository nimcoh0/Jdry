package org.softauto.grpc;


import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class RpcProvider implements PluginProvider {


    @Override
    public Provider create() {
       return RpcProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "RPC";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
