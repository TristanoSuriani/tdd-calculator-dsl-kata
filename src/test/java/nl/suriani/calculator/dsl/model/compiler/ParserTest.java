package nl.suriani.calculator.dsl.model.compiler;

import nl.suriani.calculator.dsl.model.expression.Expression;
import nl.suriani.calculator.dsl.model.expression.Number;
import nl.suriani.calculator.dsl.model.expression.Operation;
import nl.suriani.calculator.dsl.model.expression.Operator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest implements TestTokens {

    private final Parser parser = new Parser();

    @Test
    void emptyStream() {
        var inputTokens = List.<Token>of();
        var expression = parser.parse(inputTokens);

        thenTheExpression(expression)
                .looksLikeThis(numberExpression(0));
    }

    @Test
    void onlyNumber() {
        var inputTokens = List.of(number("1"));
        var expression = parser.parse(inputTokens);

        thenTheExpression(expression)
                .looksLikeThis(numberExpression(1));
    }

    @Test
    void sumOfTwoOperands() {
        var inputTokens = List.of(number("1"), number("2"), operator("+"));
        var expression = parser.parse(inputTokens);

        thenTheExpression(expression)
                .looksLikeThis(operation(Operator.PLUS, numberExpression(1), numberExpression(2)));
    }

    private Number numberExpression(int value) {
        return new Number(BigDecimal.valueOf(value));
    }

    private Operation operation(Operator operator, Expression... operands) {
        return new Operation(operator, Arrays.asList(operands));
    }

    private ThenTheExpression thenTheExpression(Expression expression) {
        return new ThenTheExpression(expression);
    }

    private record ThenTheExpression(Expression expression) {
        public void looksLikeThis(Expression other) {
            assertEquals(other, expression);
        }
    }
}