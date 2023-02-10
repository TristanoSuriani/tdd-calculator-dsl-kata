package nl.suriani.calculator.dsl.model.calculator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface TestTokens {
    default Token number(String value) {
        return new Token(TokenType.NUMBER, value);
    }

    default Token operator(String value) {
        return new Token(TokenType.OPERATOR, value);
    }

    default Token identifier(String value) {
        return new Token(TokenType.IDENTIFIER, value);
    }

    default Token parenthesis(String parenthesis) {
        return switch (parenthesis) {
            case "(" -> new Token(TokenType.L_PAREN, parenthesis);
            case ")" -> new Token(TokenType.R_PAREN, parenthesis);
            default -> throw new IllegalArgumentException(parenthesis + " is not a parenthesis");
        };
    }

    default void thenTheTokenStreamIsEmpty(List<Token> tokens) {
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    default ThenTheTokenStream thenTheTokenStream(List<Token> tokens) {
        return new ThenTheTokenStream(tokens);
    }

    record ThenTheTokenStream(List<Token> tokens) {
        public void looksLikeThis(Token... tokens) {
            assertNotNull(this.tokens);
            assertNotNull(tokens);
            assertArrayEquals(tokens, this.tokens.toArray());
        }
    }
}
