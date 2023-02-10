package nl.suriani.calculator.dsl.model.expression;

import java.math.BigDecimal;
import java.util.Arrays;

public interface TestExpression {

    default Expression operation(Operator operator, double... numbers) {
        var expressions = Arrays.stream(numbers)
                .mapToObj(BigDecimal::valueOf)
                .map(Number::new)
                .map(number -> (Expression) number)
                .toList();

        return new Operation(operator, expressions);
    }

    default Expression operation(Operator operator, Expression... expressions) {
        return new Operation(operator, Arrays.asList(expressions));
    }

    default Number number(BigDecimal n) {
        return new Number(n);
    }

    default Number number(double n) {
        return new Number(BigDecimal.valueOf(n));
    }

    default void assertAreTheSame(Number n1, Number n2) {
        if (!n1.isSameAs(n2)) {
            throw new AssertionError(String.format("Expected: <%s> but was: <%s>", n1, n2));
        }
    }
}
