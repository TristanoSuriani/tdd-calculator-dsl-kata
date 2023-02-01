package nl.suriani.calculator.dsl.model.expression;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class ExpressionTest {
    @Test
    void evaluate_number1() {
        var expression = number(1);
        assertEquals(expression, expression.evaluate());
    }

    @Test
    void evaluate_operatorPlus_number1() {
        var expression = operation(Operator.PLUS, 1);
        assertAreTheSame(number(BigDecimal.ONE), expression.evaluate());
    }

    @Test
    void evaluate_operatorMinus_number3() {
        var expression = operation(Operator.MINUS, 3);
        assertAreTheSame(number(BigDecimal.valueOf(-3)), expression.evaluate());
    }

    @Test
    void evaluate_operatorMultipliedBy_number15() {
        var expression = operation(Operator.MULTIPLIED_BY, 15);
        assertAreTheSame(number(BigDecimal.valueOf(15)), expression.evaluate());
    }

    @Test
    void evaluate_operatorDividedBy_number13() {
        var expression = operation(Operator.DIVIDED_BY, 13);
        assertAreTheSame(number(BigDecimal.valueOf(13)), expression.evaluate());
    }

    @Test
    void evaluate_operatorPlus_expression1Plus3() {
        var expression = operation(Operator.PLUS, 1, 3);
        assertAreTheSame(number(4), expression.evaluate());
    }

    @Test
    void evaluate_divideByZero() {
        var expression = operation(Operator.DIVIDED_BY, 1, 3, 0, 2);
        assertThrows(DivideByZeroException.class, expression::evaluate);
    }

    @Test
    void evaluate_nestedExpressions() { // (/ (* 2 (+ 1 (* 2 4)) 9) --> 2
        var twoMultipliedByFour =
                operation(Operator.MULTIPLIED_BY, 2, 4);

        var onePlus_TwoMultipliedByFour =
                operation(Operator.PLUS, number(1), twoMultipliedByFour);

        var twoMultipliedBy_onePlus_TwoMultipliedByFour =
                operation(Operator.MULTIPLIED_BY, number(2), onePlus_TwoMultipliedByFour);

        var expression =
               operation(Operator.DIVIDED_BY, twoMultipliedBy_onePlus_TwoMultipliedByFour, number(9));

        assertAreTheSame(number(2), expression.evaluate());
    }

    private Expression operation(Operator operator, double... numbers) {
        var expressions = Arrays.stream(numbers)
                .mapToObj(BigDecimal::valueOf)
                .map(Number::new)
                .map(number -> (Expression) number)
                .toList();

        return new Operation(operator, expressions);
    }

    private Expression operation(Operator operator, Expression... expressions) {
        return new Operation(operator, Arrays.asList(expressions));
    }

    private Number number(BigDecimal n) {
        return new Number(n);
    }

    private Number number(double n) {
        return new Number(BigDecimal.valueOf(n));
    }

    private void assertAreTheSame(Number n1, Number n2) {
        if (!n1.isSameAs(n2)) {
            throw new AssertionError(String.format("Expected: <%s> but was: <%s>", n1, n2));
        }
    }

}