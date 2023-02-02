package nl.suriani.calculator.dsl.model.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class Lexer {
    public List<Token> lex(String text) {
        var internalState = InternalState.newInstance();
        var tokens = new ArrayList<Token>();
        for (char character: normaliseText(text).toCharArray()) {
            if (isEndOfLine(character)) {
                addToken(tokens, internalState);
                internalState = internalState.reset();
                continue;
            }

            if (internalState.isInsideComment()) {
                continue;
            }

            if (isComment(character)) {
                addToken(tokens, internalState);
                internalState = internalState.reset()
                        .insideComment();
                continue;
            }

            if (isDelimiter(character)) {
                addToken(tokens, internalState);
                internalState = internalState.reset();
                continue;
            }

            if (isOperator(character)
                    || isLeftParenthesis(character)
                    || isRightParenthesis(character)) {
                internalState = handleSingleCharacterToken(internalState, tokens, character);
                continue;
            }

            if (internalState.isInsideNumber()) {
                if (isNumber("" + character)) {
                    internalState = internalState.pushCharacterToToken(character);
                    continue;
                }
                addToken(tokens, internalState);
                internalState = internalState.reset();
                continue;
            }

            if (isNumber("" + character)) {
                internalState = internalState
                        .reset()
                        .insideNumber()
                        .pushCharacterToToken(character);
                continue;
            }

            internalState = internalState.pushCharacterToToken(character);
        }

        addToken(tokens, internalState);

        return tokens;
    }

    private InternalState handleSingleCharacterToken(InternalState internalState, ArrayList<Token> tokens, char character) {
        addToken(tokens, internalState);
        internalState = internalState.reset()
                .pushCharacterToToken(character);
        addToken(tokens, internalState);
        internalState = internalState.reset();
        return internalState;
    }

    private String normaliseText(String text) {
        return text.toLowerCase()
                .replaceAll("plus", "+")
                .replaceAll("minus", "-")
                .replaceAll("multipliedby", "*")
                .replaceAll("dividedby", "/");
    }

    private boolean isDelimiter(char character) {
        var delimiters = Set.of(' ', '\t');
        return delimiters.contains(character);
    }

    private boolean isComment(char character) {
        return character == '#';
    }

    private boolean isEndOfLine(char character) {
        return character == '\n';
    }

    private boolean isOperator(char character) {
        return Set.of('+', '-', '*', '/').contains(character);
    }

    private boolean isOperator(String string) {
        return string.length() == 1 && isOperator(string.charAt(0));
    }

    private boolean isLeftParenthesis(char character) {
        return character == '(';
    }

    private boolean isLeftParenthesis(String string) {
        return string.length() == 1 && isLeftParenthesis(string.charAt(0));
    }

    private boolean isRightParenthesis(char character) {
        return character == ')';
    }

    private boolean isRightParenthesis(String string) {
        return string.length() == 1 && isRightParenthesis(string.charAt(0));
    }

    private boolean isNumber(String token) {
        var pattern = Pattern.compile( "^\\d*\\.?\\d*$");
        var matcher = pattern.matcher(token);
        return matcher.matches();
    }

    private void addToken(List<Token> tokens, InternalState internalState) {
        resolveToken(internalState)
                .ifPresent(tokens::add);
    }

    private Optional<Token> resolveToken(InternalState internalState) {
        Optional<Token> maybeToken = switch (internalState.state) {
            case OUTSIDE_SECTIONS -> Optional.empty();
            case INSIDE_NUMBER -> Optional.of(new Token(TokenType.NUMBER, internalState.token));
            case INSIDE_COMMENT -> Optional.empty();
        };

        if (maybeToken.isPresent()) {
            return maybeToken;
        }

        if(isOperator(internalState.token)) {
            return Optional.of(new Token(TokenType.OPERATOR, internalState.token));
        }

        if(isLeftParenthesis(internalState.token)) {
            return Optional.of(new Token(TokenType.L_PAREN, internalState.token));
        }

        if(isRightParenthesis(internalState.token)) {
            return Optional.of(new Token(TokenType.R_PAREN, internalState.token));
        }

        if(!internalState.token.isEmpty()) {
            return Optional.of(new Token(TokenType.IDENTIFIER, internalState.token));
        }

        return Optional.empty();
    }

    private record InternalState(String token, State state) {

        public static InternalState newInstance() {
            return new InternalState("", State.OUTSIDE_SECTIONS);
        }

        public InternalState pushCharacterToToken(char character) {
            return new InternalState(token + character, state);
        }

        public InternalState reset() {
            return newInstance();
        }

        public InternalState insideNumber() {
            return new InternalState(token, State.INSIDE_NUMBER);
        }

        public InternalState insideComment() {
            return new InternalState(token, State.INSIDE_COMMENT);
        }

        public boolean isInsideNumber() {
            return state == State.INSIDE_NUMBER;
        }

        public boolean isInsideComment() {
            return state == State.INSIDE_COMMENT;
        }
    }

    private enum State {
        OUTSIDE_SECTIONS,
        INSIDE_NUMBER,
        INSIDE_COMMENT
    }
}
