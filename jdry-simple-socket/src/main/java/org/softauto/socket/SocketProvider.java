package org.softauto.socket;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class SocketProvider implements PluginProvider {

    @Override
    public Provider create() {
        return SocketProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "SIMPLE-SOCKET";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
