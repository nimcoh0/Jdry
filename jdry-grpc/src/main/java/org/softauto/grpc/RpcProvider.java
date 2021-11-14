package org.softauto.grpc;


import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.plugin.spi.PluginTypes;

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
        return "GRPC-SERVER";
    }

    @Override
    public PluginTypes getType() {
        return PluginTypes.regular;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
