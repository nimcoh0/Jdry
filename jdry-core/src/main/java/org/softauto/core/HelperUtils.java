package org.softauto.core;


public class HelperUtils {

    public static <T> Handler<AsyncResult<T>> createHandler(T t) {
        return (res) -> {
            if (res.failed()) {

            } else if (res.result() != null && res.result().getClass().isEnum()) {

            } else {

            }

        };
    }



}
