package nl.suriani.calculator.dsl.model.compiler;

import nl.suriani.calculator.dsl.model.shared.Guards;

import java.util.List;

public record Token(TokenType type, String value) {
    public Token {
        Guards.isAssigned(type);
        Guards.isAssigned(value);
    }

    public boolean isOperator() {
        return type == TokenType.OPERATOR;
    }

    public boolean isLeftParenthesis() {
        return type == TokenType.L_PAREN;
    }

    public boolean isRightParenthesis() {
        return type == TokenType.R_PAREN;
    }

    public boolean isOperand() {
        return !isOperator()
                && !isLeftParenthesis()
                && !isRightParenthesis();
    }

    public int getPrecedence() {
        if(List.of("+", "-").contains(value)) {
            return 1;
        }

        if(List.of("*", "/").contains(value)) {
            return 2;
        }

        throw new UnsupportedOperationException("Cannot determine precedence for value " + value);
    }
}
