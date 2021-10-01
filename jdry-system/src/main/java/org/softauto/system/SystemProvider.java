package org.softauto.system;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class SystemProvider implements PluginProvider {
    @Override
    public Provider create() {
        return SystemProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getName() {
        return "SYSTEM";
    }
}
