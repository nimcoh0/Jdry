package com.baeldung.jersey.server.config;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.jersey.server.Greetings;
import com.baeldung.jersey.server.filter.ResponseServerFilter;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;

@Provider
public class HelloDynamicBinding implements DynamicFeature {

    private static final Logger LOG = LoggerFactory.getLogger(HelloDynamicBinding.class);

    @RPC
    @ListenerForTesting
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        LOG.info("Hello dynamic binding");

        if (Greetings.class.equals(resourceInfo.getResourceClass()) && resourceInfo.getResourceMethod()
            .getName()
            .contains("HiGreeting")) {
            context.register(ResponseServerFilter.class);
        }

    }

}
