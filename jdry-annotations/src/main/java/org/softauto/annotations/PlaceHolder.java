package org.softauto.annotations;

public enum PlaceHolder {

        VERIFY_RESULT_PLACE_HOLDER("verifyResultPlaceHolder"),
        EXPECTED_PLACE_HOLDER("expectedPlaceHolder"),
        RESULT_PLACE_HOLDER("resultPlaceHolder"),
        NONE("none");

    private PlaceHolder(String  value) {
        this.value = value;
    }



    private final String value;


    public static PlaceHolder fromString(String text) {
        for (PlaceHolder b : PlaceHolder.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return this.value;
    }

}
