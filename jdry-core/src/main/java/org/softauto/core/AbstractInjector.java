package org.softauto.core;

public abstract class AbstractInjector {

    public abstract Object[] inject(String fullClassName);
    public abstract void UpdateClassDescriptorArgsValues(String fullClassName,Object[] values);
}
