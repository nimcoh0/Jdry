package org.softauto.providers;

import org.softauto.annotations.Iprovider;

import java.util.Date;

public class DateProvider<T> implements Iprovider<T> {

    public T apply(T t){
        return (T)new Date();
    }

}
