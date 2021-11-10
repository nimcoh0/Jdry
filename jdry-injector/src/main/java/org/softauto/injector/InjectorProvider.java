package org.softauto.injector;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.plugin.spi.PluginTypes;

public class InjectorProvider implements PluginProvider {

    @Override
    public Provider create() {
        return InjectorProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "INJECTOR";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public PluginTypes getType() {
        return PluginTypes.regular;
    }
}
