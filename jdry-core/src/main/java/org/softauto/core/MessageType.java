package org.softauto.core;

public enum MessageType {

    method,
    variable,
    none;

    private MessageType() {
    }



    public static MessageType fromString(String text) {
        for (MessageType b : MessageType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
