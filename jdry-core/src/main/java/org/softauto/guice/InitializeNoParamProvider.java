package org.softauto.guice;

import com.google.inject.*;
import org.softauto.jvm.HeapHelper;

public class InitializeNoParamProvider {

    public static <T> Provider<T> getProvider(Class<T> type) {
        return new JnaProvider<T>(type,true);
    }

    public static <T> Provider<T> getProvider(Class<T> type,boolean fromJvm) {
        return new JnaProvider<T>(type,fromJvm);
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
               return type.newInstance();
             } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
