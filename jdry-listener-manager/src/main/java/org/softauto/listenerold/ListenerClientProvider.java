package org.softauto.listenerold;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.plugin.spi.PluginTypes;

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

    @Override
    public PluginTypes getType() {
        return PluginTypes.regular;
    }
}
