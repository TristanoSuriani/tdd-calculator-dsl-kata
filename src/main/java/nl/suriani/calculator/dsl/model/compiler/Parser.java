package nl.suriani.calculator.dsl.model.compiler;

import nl.suriani.calculator.dsl.model.expression.Expression;
import nl.suriani.calculator.dsl.model.expression.Number;
import nl.suriani.calculator.dsl.model.expression.Operation;
import nl.suriani.calculator.dsl.model.expression.Operator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.List;

public class Parser {
    public Expression parse(List<Token> tokens) {
        var stack = new ArrayDeque<Expression>();

        for (var token : tokens) {
            if (token.type() == TokenType.NUMBER) {
                var value = new BigDecimal(token.value());
                stack.push(new Number(value));
            } else if (token.type() == TokenType.OPERATOR) {
                var operator = Operator.fromValue(token.value().toUpperCase());
                var rightOperand = stack.pop();
                var leftOperand = stack.pop();
                var operation = new Operation(operator, List.of(leftOperand, rightOperand));
                stack.push(operation);
            }
        }

        return stack.isEmpty() ? new Number(BigDecimal.ZERO) : stack.pop();
    }
}
