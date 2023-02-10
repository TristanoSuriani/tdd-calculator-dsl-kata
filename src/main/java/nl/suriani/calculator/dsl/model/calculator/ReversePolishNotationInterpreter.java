package nl.suriani.calculator.dsl.model.calculator;

import nl.suriani.calculator.dsl.model.expression.Number;
import nl.suriani.calculator.dsl.model.expression.Operation;
import nl.suriani.calculator.dsl.model.expression.Operator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.List;

public class ReversePolishNotationInterpreter {

    public Number evaluate(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return new Number(BigDecimal.ZERO);
        }

        var operandsStack = new ArrayDeque<Number>();

        for (var token: tokens) {
            if(token.isOperand()) {
                operandsStack.push(number(token));
                continue;
            }

            if(token.isOperator()) {
                var secondValue = operandsStack.pop();
                var firstValue = operandsStack.pop();
                var operation = new Operation(Operator.fromValue(token.value()), List.of(firstValue, secondValue));
                operandsStack.push(operation.evaluate());
            }
        }

        if (operandsStack.size() == 1) {
            return operandsStack.pop();
        }

        throw new IncompleteExpressionException();
    }

    private Number number(Token token) {
        if (!token.isOperand()) {
            throw new IllegalStateException("Not an operand!");
        }
        return new Number(new BigDecimal(token.value()));
    }
}
