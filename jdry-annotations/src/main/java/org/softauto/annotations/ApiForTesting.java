package org.softauto.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {

    String description() default "";

    String protocol() default "RPC" ;

    String[] dependencies() default {};

    AssertType assertType() default AssertType.AssertTrue;

    String[] before() default {};

    String[] after() default {};

    Authentication authentication() default @Authentication;


}
