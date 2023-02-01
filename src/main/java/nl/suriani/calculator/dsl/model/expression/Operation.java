package nl.suriani.calculator.dsl.model.expression;

import nl.suriani.calculator.dsl.model.shared.Guards;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;

public record Operation(Operator operator, List<Expression> expressions) implements Expression {

    public Operation {
        Guards.isAssigned(operator);
        Guards.isNotEmpty(expressions);
        expressions = List.copyOf(expressions);
    }

    public Number evaluate() {
        return switch (operator) {
            case PLUS -> reduce(new Number(BigDecimal.ZERO), Number::add);
            case MINUS -> reduce(new Number(BigDecimal.ZERO), Number::subtract);
            case MULTIPLIED_BY -> reduce(new Number(BigDecimal.ONE), Number::multiplyBy);
            case DIVIDED_BY -> expressions.size() == 1
                    ? reduce(new Number(BigDecimal.ONE), Number::divideBy) // (/ 1 n)
                    : reduce(Number::divideBy);                            // (/ n1 n2 n3... nm)
        };
    }

    private Number reduce(Number seed, BinaryOperator<Number> fn) {
        return expressions.stream()
                .map(Expression::evaluate)
                .reduce(seed, fn);
    }

    private Number reduce(BinaryOperator<Number> fn) {
        return expressions.stream()
                .map(Expression::evaluate)
                .reduce(fn).orElseThrow();
    }
}
