package org.softauto.annotations;

public enum VerifyType {

    RESULT,
    ARGS,
    NONE;

    private VerifyType() {
    }



    public static VerifyType fromString(String text) {
        for (VerifyType b : VerifyType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
