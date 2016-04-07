package ua.goit.gojava.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static ua.goit.gojava.servicePackage.ElementType.*;
import ua.goit.gojava.servicePackage.ExpressionElement;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class ParametrizedSimpleCalculatorTest {

    public static final ExpressionElement OPERAND1 = new ExpressionElement(INT, 60);
    public static final ExpressionElement OPERAND2 = new ExpressionElement(INT, 15);

    private final SimpleCalculator simpleCalculator = new SimpleCalculator();

    private ExpressionElement argument1;
    private ExpressionElement argument2;
    private ExpressionElement operation;
    private int expected;

    public ParametrizedSimpleCalculatorTest(ExpressionElement argument1, ExpressionElement argument2,
                                            ExpressionElement operation, int expected) {
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.operation = operation;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> getParametrizedData() {
        return Arrays.asList(new Object[][]{
                {OPERAND1, OPERAND2, new ExpressionElement(PLUS), 75},
                {OPERAND1, OPERAND2, new ExpressionElement(MINUS), 45},
                {OPERAND1, OPERAND2, new ExpressionElement(MULTIPLY), 900},
                {OPERAND1, OPERAND2, new ExpressionElement(DIVIDE), 4},
        });
    }

    @Test
    public void test1Compute() throws Exception {

        ArrayList<ExpressionElement> expression = new ArrayList<ExpressionElement>() {{
            add(argument1);
            add(operation);
            add(argument2);
        }};

        assertEquals(expected, simpleCalculator.compute(expression));
    }

    @Test
    public void test2Compute() throws Exception {

        ArrayList<ExpressionElement> expression = new ArrayList<ExpressionElement>() {{
            add(argument1);
            add(operation);
        }};

        assertEquals(argument1.number, simpleCalculator.compute(expression));
    }

    @Test
    public void test3Compute() throws Exception {

        ArrayList<ExpressionElement> expression = new ArrayList<ExpressionElement>() {{
            add(operation);
            add(argument1);
        }};

        if (operation.elementType != MINUS) {
            assertEquals(argument1.number, simpleCalculator.compute(expression));
        } else {
            assertEquals(argument1.number * (-1), simpleCalculator.compute(expression));
        }
    }

    @Test
    public void test4Compute() throws Exception {

        ArrayList<ExpressionElement> expression = new ArrayList<ExpressionElement>() {{
            add(argument1);
        }};

            assertEquals(argument1.number, simpleCalculator.compute(expression));
    }

    @Test
    public void test5Compute() throws Exception {

        ArrayList<ExpressionElement> expression = new ArrayList<ExpressionElement>() {{
            add(operation);
        }};

        assertEquals(0, simpleCalculator.compute(expression));
    }
}