package nl.suriani.calculator.dsl.model.expression;

import nl.suriani.calculator.dsl.model.shared.Guards;

import java.math.BigDecimal;
import java.math.MathContext;

public record Number(BigDecimal value) implements Expression {

    public Number {
        Guards.isAssigned(value);
    }

    @Override
    public Number evaluate() {
        return this;
    }

    public boolean isSameAs(Number other) {
        return value.compareTo(other.value()) == 0;
    }

    public Number add(Number other) {
        return new Number(value.add(other.value()));
    }

    public Number subtract(Number other) {
        return new Number(value.subtract(other.value()));
    }

    public Number multiplyBy(Number other) {
        return new Number(value.multiply(other.value()));
    }

    public Number divideBy(Number other) {
        if (other.isSameAs(new Number(BigDecimal.ZERO))) {
            throw new DivideByZeroException();
        }
        return new Number(value.divide(other.value(), MathContext.DECIMAL32));
    }
}
