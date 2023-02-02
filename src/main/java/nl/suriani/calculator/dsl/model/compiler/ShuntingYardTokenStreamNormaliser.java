package nl.suriani.calculator.dsl.model.compiler;

import java.util.*;
import java.util.stream.Collectors;

public class ShuntingYardTokenStreamNormaliser {
    public List<Token> normalise(List<Token> inputTokens) {
        var operatorsStack = new ArrayDeque<Token>();
        var outputTokens = new ArrayList<Token>();

        for (Token token : inputTokens) {
            if (token.isOperand()) {
                outputTokens.add(token);
            } else if (token.isOperator()) {
                handleOperator(operatorsStack, outputTokens, token);
            } else if (token.isLeftParenthesis()) {
                operatorsStack.push(token);
            } else if (token.isRightParenthesis()) {
                while (!operatorsStack.isEmpty() && !operatorsStack.peek().isLeftParenthesis()) {
                    outputTokens.add(operatorsStack.pop());
                }
                if (!operatorsStack.isEmpty() && operatorsStack.peek().isLeftParenthesis()) {
                    operatorsStack.pop();
                } else {
                    throw new MismatchedParenthesisException();
                }
            }
        }

        while (!operatorsStack.isEmpty()) {
            if (operatorsStack.peek().isLeftParenthesis()) {
                throw new MismatchedParenthesisException();
            }
            outputTokens.add(operatorsStack.pop());
        }

        return outputTokens;
    }

    private static void handleOperator(ArrayDeque<Token> operatorsStack, ArrayList<Token> outputTokens, Token token) {
        while (!operatorsStack.isEmpty() &&
                operatorsStack.peek().isOperator() &&
                operatorsStack.peek().getPrecedence() >= token.getPrecedence()) {
            outputTokens.add(operatorsStack.pop());
        }
        operatorsStack.push(token);
    }
}
