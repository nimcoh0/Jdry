package org.softauto.core;

public interface AbstractInjector {

     Object[] inject(String fullClassName);

     Object[] inject(String fullClassName,Object[] args);

}
