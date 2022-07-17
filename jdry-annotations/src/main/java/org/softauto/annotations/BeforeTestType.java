package org.softauto.annotations;

public enum BeforeTestType {

    Authentication,
    None;

    public static BeforeTestType fromString(String text) {
        for (BeforeTestType b : BeforeTestType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }

}
