package nl.suriani.calculator.dsl.model.compiler;

import nl.suriani.calculator.dsl.model.expression.Number;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {
    private final Compiler compiler = new Compiler(new Lexer(), new ShuntingYardTokenStreamNormaliser(), new Parser());

    @Test
    void infix1() {
        var text = "(2+2 * 2)";

        var expression = compiler.compile(text);
        var result = expression.evaluate();

        assertAreTheSame(new Number(BigDecimal.valueOf(6)), result);
    }

    private void assertAreTheSame(Number n1, Number n2) {
        if (!n1.isSameAs(n2)) {
            throw new AssertionError(String.format("Expected: <%s> but was: <%s>", n1, n2));
        }
    }
}