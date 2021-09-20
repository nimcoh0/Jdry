package org.softauto.logger;

import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class LoggerProvider implements PluginProvider {
    @Override
    public Provider create() {
        return LoggerProviderImpl.getInstance();
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
        return "LOGGER";
    }
}
