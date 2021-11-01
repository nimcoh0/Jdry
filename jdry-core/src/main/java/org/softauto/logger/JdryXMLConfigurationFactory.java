package org.softauto.logger;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;

/**
 * log4j2 extend for trace & jdry logs. it will use SUT log4j2.xml or if not exist
 * default config file at logger module resources
 */
@Plugin(name = "JdryXMLConfigurationFactory", category = "ConfigurationFactory")
@Order(10)
public class JdryXMLConfigurationFactory extends ConfigurationFactory {


    /**
     * Valid file extensions for XML files.
     */
    public static final String[] SUFFIXES = new String[] {".xml", "*"};


    public JdryXMLConfigurationFactory() throws Exception{

    }

    /**
     * Returns the file suffixes for XML files.
     * @return An array of File extensions.
     */
    public String[] getSupportedTypes() {
        return SUFFIXES;
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return new JdryXMLConfiguration(loggerContext, source);
    }



}
