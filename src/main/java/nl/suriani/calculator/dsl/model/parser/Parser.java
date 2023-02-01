package nl.suriani.calculator.dsl.model.parser;

import nl.suriani.calculator.dsl.model.expression.Expression;

public class Parser {
    private final Lexer LEXER = new Lexer();

    public Expression parse(String text) {
        var tokens = LEXER.lex(text);
        return null;
    }
}
