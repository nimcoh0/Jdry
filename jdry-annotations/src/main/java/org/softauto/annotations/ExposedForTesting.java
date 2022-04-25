package org.softauto.annotations;



import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposedForTesting {
        String description() default "";
        String protocol() default "RPC" ;
        String[] dependencies() default {};
}
