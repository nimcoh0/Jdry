package org.softauto.annotations;

public enum Protocols {

    RPC,
    SOCKET,
    JAXRS,
    NONE;

    public static Protocols fromString(String text) {
        for (Protocols b : Protocols.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
