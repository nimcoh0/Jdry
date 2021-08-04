package org.softauto.socket;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class SocketProvider implements PluginProvider {

    @Override
    public Provider create() {
        return SocketProviderImpl.getInstance();
    }

    @Override
    public String getName() {
        return "SOCKET-NIO2";
    }

    @Override
    public String getVendor() {
        return "";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
