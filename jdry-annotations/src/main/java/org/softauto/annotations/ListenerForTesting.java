package org.softauto.annotations;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerForTesting {
    String description() default "";
    ListenerMode mode() default ListenerMode.NONE;
    String[] value() default "";
    String result() default "";

}
