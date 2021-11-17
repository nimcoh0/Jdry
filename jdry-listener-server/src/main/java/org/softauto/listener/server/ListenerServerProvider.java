package org.softauto.listener.server;

import org.softauto.plugin.ProviderScope;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.plugin.spi.PluginTypes;

public class ListenerServerProvider implements PluginProvider {
    @Override
    public Provider create() {
        return ListenerServerProviderImpl.getInstance();
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
        return "LISTENER-SERVER";
    }

    @Override
    public PluginTypes getType() {
        return PluginTypes.regular;
    }

    @Override
    public ProviderScope scope() {
        return ProviderScope.Tester;
    }
}
