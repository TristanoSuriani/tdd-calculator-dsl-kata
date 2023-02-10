package nl.suriani.calculator.dsl.model.calculator;

import nl.suriani.calculator.dsl.model.expression.TestExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest implements TestExpression {
    private final Lexer lexer = new Lexer();
    private final ShuntingYardTokenStreamNormaliser shuntingYardTokenStreamNormaliser = new ShuntingYardTokenStreamNormaliser();
    private final ReversePolishNotationInterpreter reversePolishNotationInterpreter = new ReversePolishNotationInterpreter();
    private final Calculator calculator = new Calculator(reversePolishNotationInterpreter, shuntingYardTokenStreamNormaliser, lexer);

    @Test
    void nothing() {
        var text = "";
        var result = calculator.calculate(text);
        assertEquals("0", result);
    }

    @Test
    void number() {
        var text = "2";
        var result = calculator.calculate(text);
        assertEquals("2", result);
    }

    @Test
    void simpleOperation() {
        var text = "2 + 3";
        var result = calculator.calculate(text);
        assertEquals("5", result);
    }

    @Test
    void simpleDivision() {
        var text = "4 / 2";
        var result = calculator.calculate(text);
        assertEquals("2", result);
    }

    @Test
    void divisionByZero() {
        var text = "4 / 0";
        var result = calculator.calculate(text);
        assertEquals("error", result);
    }

    @Test
    void expressionWithParenthesisAndMoreOperands() {
        var text = "1 + ((2) + 3)";
        var result = calculator.calculate(text);
        assertEquals("6", result);
    }

    @Test
    void expressionWithPrecedenceTrick() {
        var text = "2 + 2 * 2";
        var result = calculator.calculate(text);
        assertEquals("6", result);
    }

    @Test
    void expressionWithHigherPrecedenceButAlsoParenthesis() {
        var text = "(2 + 2) * 2";
        var result = calculator.calculate(text);
        assertEquals("8", result);
    }

    @Test
    void sExpressions() {
        var text = "(+ 1 (* 3 2))";
        var result = calculator.calculate(text);
        assertEquals("7", result);
    }
}