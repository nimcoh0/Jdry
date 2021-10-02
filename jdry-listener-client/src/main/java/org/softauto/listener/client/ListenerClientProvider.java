package org.softauto.listener.client;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class ListenerClientProvider implements PluginProvider {
    @Override
    public Provider create() {
        return ListenerClientProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return "";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getName() {
        return "LISTENER-CLIENT";
    }
}
