package nl.suriani.calculator.dsl.model.calculator;

import nl.suriani.calculator.dsl.model.expression.Number;
import nl.suriani.calculator.dsl.model.expression.TestExpression;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReversePolishNotationInterpreterTest implements TestTokens, TestExpression {
    private final ReversePolishNotationInterpreter reversePolishNotationInterpreter = new ReversePolishNotationInterpreter();

    @Test
    void empty() {
        var tokens = List.<Token>of();

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(new Number(BigDecimal.ZERO), result);
    }

    @Test
    void oneNumber() {
        var tokens = List.of(number("1"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(1), result);
    }

    @Test
    void twoNumbers() {
        var tokens = List.of(number("1"), number("2"));

        assertThrows(IncompleteExpressionException.class, () -> reversePolishNotationInterpreter.evaluate(tokens));
    }

    @Test
    void twoNumbersAndPlus() {
        var tokens = List.of(number("1"), number("2"), operator("+"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(3), result);
    }

    @Test
    void twoNumbersAndMinus() {
        var tokens = List.of(number("1"), number("2"), operator("-"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(-1), result);
    }

    @Test
    void twoNumbersAndMultipliedBy() {
        var tokens = List.of(number("1"), number("2"), operator("*"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(2), result);
    }

    @Test
    void twoNumbersAndDividedBy() {
        var tokens = List.of(number("1"), number("2"), operator("/"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(0.5), result); //TODO: fails
    }

    @Test
    void threeOperandsAndTwoOperatorsWithLowPrecedence() {
        var tokens = List.of(number("1"), number("2"), operator("+"),
                number("5"), operator("-"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(-2), result);
    }

    @Test
    void threeOperandsAndTwoOperatorsWithHighPrecedence() {
        var tokens = List.of(number("1"), number("2"), operator("*"),
                number("2"), operator("/"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(1), result);
    }

    @Test
    void precedenceMixAndMatch() {
        var tokens = List.of(number("2"), number("2"), number("2"), operator("*"),
                operator("+"));

        var result = reversePolishNotationInterpreter.evaluate(tokens);

        assertAreTheSame(number(6), result);
    }

    /*
        other tests:
        precedence mix and match
        expressions with parenthesis
        mismatched parenthesis (should lead to incomplete?)
     */
}