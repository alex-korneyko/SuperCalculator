package ua.goit.gojava.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.goit.gojava.servicePackage.ExpressionElement;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class ParametrizedParserTest {

    String stringExpression;
    List<ExpressionElement> expression;

    public ParametrizedParserTest(String stringExpression, List<ExpressionElement> expression) {
        this.stringExpression = stringExpression;
        this.expression = expression;
    }

    @Test
    public void testToIntOperands() throws Exception {

    }
}