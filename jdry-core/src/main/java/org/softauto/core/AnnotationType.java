package org.softauto.core;

public enum AnnotationType {

    VerifyForTesting,
    ListenerForTesting,
    ExposedForTesting,
    None;


    private AnnotationType() {
    }



    public static AnnotationType fromString(String text) {
        for (AnnotationType b : AnnotationType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }


}
