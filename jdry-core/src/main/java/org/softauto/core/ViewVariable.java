package org.softauto.core;


public enum ViewVariable {

    ASSERT_RESULT_VARIABLE("assert_result_variable"),
    VERIFY_RESULT("verify_result"),
    EXPECTED_VARIABLE("expected_variable"),
    EXPECTED("expected"),
    STEP_RESULT("step_result"),
    STEP_ARGS_VARIABLE("step_args_variable"),
    API_RESULT("api_result"),
    API_ARGS_VARIABLE("api_args_variable"),
    LISTENER_RESULT("listener_result"),
    LISTENER_ARGS_VARIABLE("listener_args_variable"),
    NONE("none");

    private ViewVariable(String  value) {
        this.value = value;
    }



    private final String value;


    public static ViewVariable fromString(String text) {
        for (ViewVariable b : ViewVariable.values()) {
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
