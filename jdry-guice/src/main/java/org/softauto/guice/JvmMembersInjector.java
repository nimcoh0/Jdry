package org.softauto.guice;

import com.google.inject.MembersInjector;
import org.softauto.jvm.HeapHelper;

import java.lang.reflect.Field;

public class JvmMembersInjector<T> implements MembersInjector<T> {

    private Class clazz;
    private Object obj;
    private Field field;


    public JvmMembersInjector(Field field,Class clazz) {
        this.field = field;
        //this.obj = HeapHelper.getInstances(clazz)[0];
        //clazz.set.setAccessible(true);
    }

    @Override
    public void injectMembers(T t) {
        try {
            this.obj = HeapHelper.getInstances(t.getClass())[0];
            //field.set(t, obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
