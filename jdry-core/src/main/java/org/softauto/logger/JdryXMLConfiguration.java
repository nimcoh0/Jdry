package org.softauto.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

import java.io.Serializable;

/**
 * extend log4j2 configuration for jdry & trace logs
 */
public class JdryXMLConfiguration extends XmlConfiguration{

    String fileName =System.getProperty("user.dir") +"/jdry.log";
    String loggerName = "org.softauto";

    public JdryXMLConfiguration(final LoggerContext loggerContext, final ConfigurationSource configSource) {
        super(loggerContext,configSource);
    }

    @Override
    protected void doConfigure() {
        super.doConfigure();
        LoggerContext ctx = (LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        ConfigurationBuilder<BuiltConfiguration> builder = new DefaultConfigurationBuilder();
        Appender rolling =  createRollingAppender();
        Filter tracerFilter = MarkerFilter.createFilter("TRACER", Filter.Result.ACCEPT, Filter.Result.DENY);
        Filter jdryFilter = MarkerFilter.createFilter("JDRY", Filter.Result.ACCEPT, Filter.Result.DENY);
        final Layout jdryLayout = PatternLayout.newBuilder().withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} sut     %-5level %-30notEmpty{[%marker]} %tid - %msg%xEx - %class{36} %L %M%n").build();
        final Layout traceLayout = PatternLayout.newBuilder().withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} sut     %-5level %-30notEmpty{[%marker]} %tid - %msg%xEx -  %n").build();
        final Appender appender = FileAppender.createAppender(fileName, "false", "false", "jdry", "true",
                "false", "false", "4000", jdryLayout, null, "false", null, config);
        SocketAppender socketAppender = SocketAppender.createAppender("localhost", "23", "UDP", null, 3000, "3000", "false", "sock", "false", "false", jdryLayout, null, "false",  config);
        appender.start();
        socketAppender.start();
        rolling.start();
        addAppender(appender);
        addAppender(rolling );
        addAppender(socketAppender );
        AppenderRef ref = AppenderRef.createAppenderRef("jdry", null, null);
        AppenderRef rollingRef = AppenderRef.createAppenderRef("rolling", null, null);
        AppenderRef sockRef = AppenderRef.createAppenderRef("sock", null, null);
        AppenderRef[] refs = new AppenderRef[] {ref,rollingRef,sockRef};
        LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.ALL, loggerName,"true", refs, null, config, null );
        //loggerConfig.addAppender(appender, Level.DEBUG, jdryFilter);
        loggerConfig.addAppender(appender, Level.DEBUG, null);
        loggerConfig.addAppender(socketAppender, Level.DEBUG, null);
        loggerConfig.addAppender(rolling, Level.TRACE, tracerFilter);
        addLogger(loggerName, loggerConfig);
        }

    static Appender createRollingAppender() {
        ConfigurationBuilder<BuiltConfiguration> builder = new DefaultConfigurationBuilder();
        builder.setStatusLevel( Level.ERROR);
        builder.setConfigurationName("RollingBuilder");
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n");
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("ForceTriggerPolicy"));
        AppenderComponentBuilder appenderBuilder = builder.newAppender("rolling", "RollingFile")
                .addAttribute("fileName", "trace.log")
                .addAttribute("filePattern", "archive/${sys:logFilename}.log.gz")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy);
        builder.add(appenderBuilder);
        return builder.build().getAppender("rolling");
    }

}
