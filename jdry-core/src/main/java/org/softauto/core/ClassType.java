package org.softauto.core;

/**
 * supported class type
 */
public enum ClassType {

    SINGLETON,
    //INSTANCE,
    INITIALIZE,
    INITIALIZE_NO_PARAM,
    //INNER_STATIC,
    //INNER,
    NONE;

    private ClassType() {
    }



    public static ClassType fromString(String text) {
        for (ClassType b : ClassType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
