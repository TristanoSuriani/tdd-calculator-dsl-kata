package nl.suriani.calculator.dsl.model.calculator;

public class Calculator {
    private final ReversePolishNotationInterpreter reversePolishNotationInterpreter;
    private final ShuntingYardTokenStreamNormaliser shuntingYardTokenStreamNormaliser ;
    private final Lexer lexer;

    /*
        Known limitations:

        s-expressions:
        It is possible to resolve s-espressions but with max 2 operands per expression.
        A possibility would be to determine in advance if a certain token stream represents a valid s-expression
        before applying the shunting yard algorithm, and then using an s-expression interpreter instead of a RPN one.

        reverse polish notation:
        likewise, the shunting yard algorithm messes up a token stream that already represent an expression in RPN.
        Here it would be possible to analyse the token stream and recognise a valid RPN expression and interpreting it
        directly without first applying the shunting yard algorithm.
     */

    public Calculator(ReversePolishNotationInterpreter reversePolishNotationInterpreter, ShuntingYardTokenStreamNormaliser shuntingYardTokenStreamNormaliser, Lexer lexer) {
        this.reversePolishNotationInterpreter = reversePolishNotationInterpreter;
        this.shuntingYardTokenStreamNormaliser = shuntingYardTokenStreamNormaliser;
        this.lexer = lexer;
    }

    public String calculate(String expression) {
        var tokens = shuntingYardTokenStreamNormaliser.normalise(lexer.lex(expression));
        try {
            return reversePolishNotationInterpreter.evaluate(tokens).value().toString();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "error";
        }
    }
}
