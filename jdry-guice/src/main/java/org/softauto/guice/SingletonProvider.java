package org.softauto.guice;

import com.google.inject.Provider;
import org.softauto.jvm.HeapHelper;

import java.lang.reflect.Method;

public class SingletonProvider {

    public static <T> Provider<T> getProvider(Class<T> type) {
        return new SingletonProvider.JnaProvider<T>(type,true);
    }

    public static <T> Provider<T> getProvider(Class<T> type,boolean fromJvm) {
        return new SingletonProvider.JnaProvider<T>(type,fromJvm);
    }
    static class JnaProvider<T> implements Provider<T> {

        final Class<T> type;
        boolean fromJvm;


        public JnaProvider(Class<T> type,boolean fromJvm) {
            this.type = type;
            this.fromJvm = fromJvm;
        }

        @Override
        public T get() {
            try {
                if(fromJvm) {
                    Object[] o = HeapHelper.getInstances(type);
                    if (o != null && o.length > 0) {
                        return (T) o[0];
                    }
                }
                Method singleton =  type.getDeclaredMethod("getInstance");
                return (T)singleton.invoke(type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
