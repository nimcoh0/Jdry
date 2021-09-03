package org.softauto.cucumber;

public enum CucumberTypesParameter {

    INT,
    FLOAT,
    WORD,
    STRING,
    DOUBLE,
    LONG,
    SHORT,
    BYTE;




    public static CucumberTypesParameter fromString(String text) {
        for (CucumberTypesParameter b : CucumberTypesParameter.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }

}
