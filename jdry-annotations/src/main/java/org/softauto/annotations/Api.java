package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

    String description() default "";
    String protocol() default "RPC" ;
    String[] dependencies() default {};
    AssertType assertType() default AssertType.AssertTrue;
}
