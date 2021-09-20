package org.softauto.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * helper class
 */
public class Log4j2Utils {

    private static final Logger logger = LogManager.getLogger(Log4j2Utils.class);

    /**
     * change log level at run time
     * @param level
     */
    public static void changeLogLevel(String level){
        try {
            LoggerContext ctx = (LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(org.apache.logging.log4j.LogManager.ROOT_LOGGER_NAME);
            loggerConfig.setLevel(Level.getLevel(level));
            ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
            logger.debug("log level change to " + level);
        }catch (Exception e){
            logger.error("fail to change log level to " + level);
        }
    }
}
