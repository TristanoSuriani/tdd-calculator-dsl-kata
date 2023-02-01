package nl.suriani.calculator.dsl.model.parser;

import nl.suriani.calculator.dsl.model.shared.Guards;

public record Token(TokenType type, String value) {
    public Token {
        Guards.isAssigned(type);
        Guards.isAssigned(value);
    }
}
