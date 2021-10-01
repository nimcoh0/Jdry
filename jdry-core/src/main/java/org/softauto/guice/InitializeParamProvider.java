package org.softauto.guice;

import com.google.inject.Provider;
import org.softauto.jvm.HeapHelper;

public class InitializeParamProvider {

    public static <T> Provider<T> getProvider(Class<T> type,Class[] types,Object[] args) {
        return new JnaProvider<T>(type,types, args,true);
    }

    public static <T> Provider<T> getProvider(Class<T> type,Class[] types,Object[] args,boolean fromJvm) {
        return new JnaProvider<T>(type,types,args,fromJvm);
    }
    static class JnaProvider<T> implements Provider<T> {

        final Class<T> type;
        boolean fromJvm;
        Class[] types;
        Object[] args;

        public JnaProvider(Class<T> type,Class[] types,Object[] args,boolean fromJvm) {
            this.type = type;
            this.fromJvm = fromJvm;
            this.types = types;
            this.args = args;
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
               return type.getConstructor(types).newInstance(args);
             } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
