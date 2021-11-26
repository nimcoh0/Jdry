package org.softauto.logger;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

@Plugin(
        name = "JdryAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class JdryAppender extends AbstractAppender {


    protected JdryAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    protected JdryAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static JdryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new JdryAppender(name, filter);
    }

    @PluginFactory
    public static JdryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("layout") Layout layout,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
            @PluginElement("properties") Property[] properties){
        return new JdryAppender(name, filter,layout,ignoreExceptions,properties);
    }

    @Override
    public void append(LogEvent event) {
        org.softauto.logger.impl.Logger.log(event.getLevel().name(),event.getMarker() != null ? event.getMarker().getName() : ""
                ,event.getMessage().getFormattedMessage(),event.getSource().getClassName(),
                event.getThrown() != null ? event.getThrown().getMessage() : "");
    }
}
