package org.softauto.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.util.Arrays;

public class JvmTypeListener implements TypeListener {

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        Class<?> clazz = typeLiteral.getRawType();
        while (clazz != null) {
            Class<?> finalClazz = clazz;
            Arrays.stream(clazz.getDeclaredFields())
                    .forEach(f -> typeEncounter
                                    .register(new JvmMembersInjector<I>(f, finalClazz)));
            clazz = clazz.getSuperclass();
        }
    }
}
