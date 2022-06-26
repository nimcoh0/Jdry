package org.softauto.core;

import org.softauto.annotations.AssertType;

public class Assert extends org.junit.Assert {

    public static void AssertThat(Object result, Object expected, AssertType assertType){

    }

    public boolean isSuccesses(){
        return true;
    }
}
