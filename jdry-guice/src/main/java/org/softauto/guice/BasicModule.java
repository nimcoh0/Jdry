package org.softauto.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {

        bindListener(Matchers.any(), new JvmTypeListener());
        //bind(org.pack.processor.db.DBService.class);
        //JvmInjector listener = new JvmInjector();
        //bindListener(Matchers.any(), listener);
        //bind(Object.class).toProvider(JvmProvider.fromJna(Object.class,"zz"));
        //binder().bind(new TypeLiteral<Class<?>>() {}).toInstance(testObject.getClass());
    }
}
