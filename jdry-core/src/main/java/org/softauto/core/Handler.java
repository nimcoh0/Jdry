package org.softauto.core;

import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface Handler<E> {
    void handle(E var1) throws Exception;
}
