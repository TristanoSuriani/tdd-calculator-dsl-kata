package nl.suriani.calculator.dsl.model.expression;

import nl.suriani.calculator.dsl.model.shared.Guards;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;

public record Operation(Operator operator, List<Expression> operands) implements Expression {

    public Operation {
        Guards.isAssigned(operator);
        Guards.isNotEmpty(operands);
        operands = List.copyOf(operands);
    }

    public Number evaluate() {
        return switch (operator) {
            case PLUS -> reduce(new Number(BigDecimal.ZERO), Number::add);
            case MINUS -> operands.size() == 1
                    ? reduce(new Number(BigDecimal.ZERO), Number::subtract) // (- n)
                    : reduce(Number::subtract);                             // (- n1 n2 n3... nm)
            case MULTIPLIED_BY -> reduce(new Number(BigDecimal.ONE), Number::multiplyBy);
            case DIVIDED_BY -> operands.size() == 1
                    ? reduce(new Number(BigDecimal.ONE), Number::divideBy) // (/ 1 n)
                    : reduce(Number::divideBy);                            // (/ n1 n2 n3... nm)
        };
    }

    private Number reduce(Number seed, BinaryOperator<Number> fn) {
        return operands.stream()
                .map(Expression::evaluate)
                .reduce(seed, fn);
    }

    private Number reduce(BinaryOperator<Number> fn) {
        return operands.stream()
                .map(Expression::evaluate)
                .reduce(fn).orElseThrow();
    }
}
