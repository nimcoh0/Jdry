package org.softauto.logger;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

/**
 * extend log4j2 configuration for jdry & trace logs
 */
public class JdryXMLConfiguration1 extends XmlConfiguration{


    public JdryXMLConfiguration1(final LoggerContext loggerContext, final ConfigurationSource configSource) {
        super(loggerContext,configSource);
    }

    @Override
    protected void doConfigure() {
        super.doConfigure();
    }



}
