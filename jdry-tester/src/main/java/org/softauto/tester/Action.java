package org.softauto.tester;

@FunctionalInterface
public interface Action<R> {

    R exec();

}
