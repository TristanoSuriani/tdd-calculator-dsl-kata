package nl.suriani.calculator.dsl.model.compiler;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ShuntingYardTokenStreamNormaliserTest implements TestTokens {
    private final ShuntingYardTokenStreamNormaliser normaliser = new ShuntingYardTokenStreamNormaliser();

    @Test
    void emptyStream() {
        var inputTokens = List.<Token>of();
        var outputTokens = normaliser.normalise(inputTokens);
        thenTheTokenStreamIsEmpty(outputTokens);
    }

    @Test
    void onlyOneNumber() {
        var inputTokens = List.of(number("1"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens).looksLikeThis(number("1"));
    }

    @Test
    void twoNumbers() {
        var inputTokens = List.of(number("1"), number("2"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens).looksLikeThis(number("1"), number("2"));
    }

    @Test
    void infixSumWithTwoOperands() {
        var inputTokens = List.of(number("1"), operator("+"), number("2"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens).looksLikeThis(number("1"), number("2"), operator("+"));
    }

    @Test
    void infixSumWithThreeOperands() {
        var inputTokens = List.of(number("1"), operator("+"), number("2"), operator("-"),
                number("3"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), operator("+"), number("3"),
                        operator("-"));
    }

    @Test
    void infixSumWithParenthesis() {
        var inputTokens = List.of(parenthesis("("), number("1"), operator("+"), number("2"),
                parenthesis(")"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), operator("+"));
    }

    @Test
    void infixExpressionWithDifferentPrecedence() {
        var inputTokens = List.of(number("1"), operator("+"), number("2"), operator("*"),
                number("3"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), number("3"), operator("*"),
                        operator("+"));
    }

    @Test
    void infixExpressionWithDifferentPrecedenceButWithParenthesisAsWell() {
        var inputTokens = List.of(parenthesis("("), number("1"), operator("+"), number("2"),
                parenthesis(")"), operator("*"), number("3"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), operator("+"), number("3"),
                        operator("*"));
    }

    @Test
    void mismatchedParenthesis1() {
        var inputTokens = List.of(parenthesis("("), number("1"), operator("+"), number("2"));

        assertThrows(MismatchedParenthesisException.class, () -> normaliser.normalise(inputTokens));
    }

    @Test
    void mismatchedParenthesis2() {
        var inputTokens = List.of(number("1"), operator("+"), number("2"), parenthesis(")"));

        assertThrows(MismatchedParenthesisException.class, () -> normaliser.normalise(inputTokens));
    }

    @Test
    void unneededParenthesis() {
        var inputTokens = List.of(number("1"), parenthesis("("), parenthesis(")"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"));
    }

    @Test
    void sExpression1() {
        var inputTokens = List.of(parenthesis("("), operator("+"), number("1"), number("2"),
                parenthesis(")"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), operator("+"));
    }

    @Test
    void sExpression2() {
        var inputTokens = List.of(parenthesis("("), operator("+"), number("1"),
                parenthesis("("), operator("*"), number("2"), number("3"), parenthesis(")"),
                parenthesis(")"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), number("3"),
                        operator("*"), operator("+"));
    }

    @Test
    void sExpression3() {
        var inputTokens = List.of(parenthesis("("), operator("+"), number("1"), number("2"),
                number("3"), parenthesis(")"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("2"), number("3"), operator("+"));
    }

    @Test
    void identifiers() {
        var inputTokens = List.of(identifier("Fizz"), operator("+"), identifier("Buzz"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(identifier("Fizz"), identifier("Buzz"), operator("+"));
    }

    @Test
    void onlyOperands() {
        var inputTokens = List.of(identifier("Fizz"), number("3"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(identifier("Fizz"), number("3"));
    }

    @Test
    void reversePolishNotation1() {
        var inputTokens = List.of(number("1"), number("3"), operator("+"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("3"), operator("+"));
    }

    @Test
    void reversePolishNotation2() {
        var inputTokens = List.of(number("1"), number("3"), operator("*"), operator("+"));

        var outputTokens = normaliser.normalise(inputTokens);

        thenTheTokenStream(outputTokens)
                .looksLikeThis(number("1"), number("3"), operator("*"), operator("+"));
    }
}