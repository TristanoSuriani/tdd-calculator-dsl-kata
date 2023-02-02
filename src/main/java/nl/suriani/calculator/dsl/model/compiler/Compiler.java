package nl.suriani.calculator.dsl.model.compiler;

import nl.suriani.calculator.dsl.model.expression.Expression;

public class Compiler {
    private final Lexer lexer;
    private final ShuntingYardTokenStreamNormaliser normaliser;
    private final Parser parser;

    public Compiler(Lexer lexer, ShuntingYardTokenStreamNormaliser normaliser, Parser parser) {
        this.lexer = lexer;
        this.normaliser = normaliser;
        this.parser = parser;
    }

    public Expression compile(String text) {
        var tokens = lexer.lex(text);
        tokens = normaliser.normalise(tokens);
        return parser.parse(tokens);
    }
}
