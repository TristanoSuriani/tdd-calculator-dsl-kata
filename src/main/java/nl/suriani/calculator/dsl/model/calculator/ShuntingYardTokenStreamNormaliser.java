package nl.suriani.calculator.dsl.model.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ShuntingYardTokenStreamNormaliser {
    /*
    * Coauthored with ChatGPT, which did the heavy lifting.
    * */

    public List<Token> normalise(List<Token> inputTokens) {
        var operatorsStack = new ArrayDeque<Token>();
        var outputTokens = new ArrayList<Token>();

        for (Token token : inputTokens) {
            if (token.isOperand()) {
                outputTokens.add(token);
                continue;
            }

            if (token.isOperator()) {
                handleOperator(operatorsStack, outputTokens, token);
                continue;
            }

            if (token.isLeftParenthesis()) {
                operatorsStack.push(token);
                continue;
            }

            if (token.isRightParenthesis()) {
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

    private static void handleOperator(Deque<Token> operatorsStack, List<Token> outputTokens, Token token) {
        while (!operatorsStack.isEmpty() &&
                operatorsStack.peek().isOperator() &&
                operatorsStack.peek().getPrecedence() >= token.getPrecedence()) {
            outputTokens.add(operatorsStack.pop());
        }
        operatorsStack.push(token);
    }
}
