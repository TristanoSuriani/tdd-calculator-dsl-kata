package nl.suriani.calculator.dsl.model.expression;

import java.util.Arrays;

public enum Operator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLIED_BY("*"),
    DIVIDED_BY("/");

    private String value;

    Operator(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Operator fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown operator " + value));
    }
}
