package nl.suriani.calculator.dsl.model.compiler;

public class Compiler {
    private final Lexer lexer;
    private final ShuntingYardTokenStreamNormaliser normaliser;
    private final Parser parser;

    public Compiler(Lexer lexer, ShuntingYardTokenStreamNormaliser normaliser, Parser parser) {
        this.lexer = lexer;
        this.normaliser = normaliser;
        this.parser = parser;
    }
}
