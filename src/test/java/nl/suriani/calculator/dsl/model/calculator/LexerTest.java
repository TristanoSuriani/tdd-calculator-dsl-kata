package nl.suriani.calculator.dsl.model.calculator;

import org.junit.jupiter.api.Test;

class LexerTest implements TestTokens {
    private final Lexer lexer = new Lexer();

    @Test
    void emptyText() {
        var text = "";
        var tokens = lexer.lex(text);

        thenTheTokenStreamIsEmpty(tokens);
    }

    @Test
    void singleLine_onlySpaces() {
        var text = "   ";
        var tokens = lexer.lex(text);

        thenTheTokenStreamIsEmpty(tokens);
    }

    @Test
    void singleLine_onlySpacesAndComments() {
        var text = "  #blablabla ";
        var tokens = lexer.lex(text);

        thenTheTokenStreamIsEmpty(tokens);
    }

    @Test
    void singleLine_onlyOneNumberWithOneDigit() {
        var text = "1";
        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("1"));
    }

    @Test
    void singleLine_onlyOneNumberWithMoreDigitsAndDecimals() {
        var text = "12.03";
        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("12.03"));
    }

    @Test
    void singleLine_onlyOneNumberWithMoreDigitsAndDecimalsAndComment() {
        var text = "12.03 #this should work.123123123";
        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("12.03"));
    }

    @Test
    void singleLine_infixOperationWithTwoOperands() {
        var text = "12.03 + 2";
        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("12.03"), operator("+"), number("2"));
    }

    @Test
    void multiline_infixOperationWithTwoOperands() {
        var text = """
             12.03 +
             2""";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("12.03"), operator("+"), number("2"));
    }

    @Test
    void multiline_multipleOperations_noParenthesis() {
        var text = """
             12.03 +
             2 *
             1.5
             / 2""";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("12.03"), operator("+"),
                        number("2"), operator("*"),
                        number("1.5"),
                        operator("/"), number("2"));
    }

    @Test
    void multiline_multipleOperations_noParenthesis_withAliases() {
        var text = """
             12.03 plus
             2 multipliedBy
             1.5
             DiViDeDBy 2""";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("12.03"), operator("+"),
                        number("2"), operator("*"),
                        number("1.5"),
                        operator("/"), number("2"));
    }

    @Test
    void looksWeirdButTheLexerMakesNoJudgement() {
        var text = "-0.1++- * / 2";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(operator("-"), number("0.1"), operator("+"),
                        operator("+"), operator("-"), operator("*"),
                        operator("/"), number("2"));
    }

    @Test
    void expressionsWithParenthesis() {
        var text = "2 + (1/2) * (1 -3)";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(number("2"), operator("+"),
                        parenthesis("("), number("1"), operator("/"), number("2"), parenthesis(")"), operator("*"),
                        parenthesis("("), number("1"), operator("-"), number("3"), parenthesis(")"));
    }

    @Test
    void sEspression() {
        var text = "(+ 2 (* (/ 1 2) (- 1 3)))";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(parenthesis("("),  operator("+"),  number("2"),
                        parenthesis("("), operator("*"),
                        parenthesis("("),  operator("/"), number("1"), number("2"), parenthesis(")"),
                        parenthesis("("),  operator("-"), number("1"), number("3"), parenthesis(")"),
                        parenthesis(")"), parenthesis(")"));
    }

    @Test
    void multilineWithCommentAndIdentifiers() {
        var text = """
                Fizz #comment
                15
                Buzz
                -> 0""";

        var tokens = lexer.lex(text);

        thenTheTokenStream(tokens)
                .looksLikeThis(identifier("fizz"),
                        number("15"),
                        identifier("buzz"),
                        operator("-"), identifier(">"), number("0"));
    }
}