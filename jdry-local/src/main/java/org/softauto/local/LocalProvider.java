package org.softauto.local;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class LocalProvider implements PluginProvider {

    @Override
    public Provider create() {
        return LocalProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "LOCAL";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
