package ua.goit.gojava.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.goit.gojava.servicePackage.ExpressionElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static ua.goit.gojava.servicePackage.ElementType.*;

@RunWith(value = Parameterized.class)
public class ParametrizedParserTest {

    //Объект парсера
    private final Parser parser = new Parser();

    String stringExpression;                    //Строка с выражением
    List<ExpressionElement> expression;         //Выражение в виде коллекции мат-объектов ExpressionElement

    public ParametrizedParserTest(String stringExpression, List<ExpressionElement> expression) {
        this.stringExpression = stringExpression;
        this.expression = expression;
    }

    /**
     * Список входных и ожидаемых выходных параметорв для параметризованных тестов.
     * Выходным параметром является коллекция объектов ExpressionElement,
     * полученная в результате парсинга входной строки String,
     * введённая пользователем с клавиатуры.
     *
     * @return Перечисление одномерных массивов с параметрами
     */
    @Parameterized.Parameters
    public static Iterable<Object[]> getParametrizedData() {
        return Arrays.asList(new Object[][]{
                //Тест на опредиление числа
                {"12582", new ArrayList<ExpressionElement>() {{
                    add(new ExpressionElement(INT, 12582));
                }}},

                //Тест на игнорирование мат-операций без чисел
                {"+-*/", new ArrayList<ExpressionElement>()},

                //Тест на опредиление скобок
                {"()", new ArrayList<ExpressionElement>() {{
                    add(new ExpressionElement(OPEN_PARENTHESIS));
                    add(new ExpressionElement(CLOSE_PARENTHESIS));
                }}},

                //Тест на опредиление отрицательного числа
                {"-5", new ArrayList<ExpressionElement>() {{
                    add(new ExpressionElement(INT, -5));
                }}},

                //Тест на сложение с отрицательным числом, которое не в скобках
                {"5+-4", new ArrayList<ExpressionElement>() {{
                    add(new ExpressionElement(INT, 5));
                    add(new ExpressionElement(PLUS));
                    add(new ExpressionElement(INT, (-4)));
                }}},

                //Тест на сложение с отрицательным числом, которое в скобках
                {"5+(-4)", new ArrayList<ExpressionElement>() {{
                    add(new ExpressionElement(INT, 5));
                    add(new ExpressionElement(PLUS));
                    add(new ExpressionElement(OPEN_PARENTHESIS));
                    add(new ExpressionElement(INT, (-4)));
                    add(new ExpressionElement(CLOSE_PARENTHESIS));
                }}},

                //Тест на опредиление всех типов мат-операций и скобок
                {"((5+(7-4))*2)/3", new ArrayList<ExpressionElement>() {{
                    add(new ExpressionElement(OPEN_PARENTHESIS));
                    add(new ExpressionElement(OPEN_PARENTHESIS));
                    add(new ExpressionElement(INT, 5));
                    add(new ExpressionElement(PLUS));
                    add(new ExpressionElement(OPEN_PARENTHESIS));
                    add(new ExpressionElement(INT, 7));
                    add(new ExpressionElement(MINUS));
                    add(new ExpressionElement(INT, (4)));
                    add(new ExpressionElement(CLOSE_PARENTHESIS));
                    add(new ExpressionElement(CLOSE_PARENTHESIS));
                    add(new ExpressionElement(MULTIPLY));
                    add(new ExpressionElement(INT, (2)));
                    add(new ExpressionElement(CLOSE_PARENTHESIS));
                    add(new ExpressionElement(DIVIDE));
                    add(new ExpressionElement(INT, (3)));
                }}}
        });
    }

    /**
     * Тесты с нормальными числовыми объектами и объектами мат-операций
     *
     * @throws Exception
     */
    @Test(timeout = 1000)
    public void test1ToIntOperands() throws Exception {

        assertEquals(expression, parser.toIntOperands(stringExpression));
    }

    /**
     * Тест в котором проверяется генерация исключения в случае нахождения
     * в строковом выражении неизвестных символов
     *
     * @throws Exception
     */
    @Test(timeout = 1000, expected = IllegalArgumentException.class)
    public void test2ToIntOperands() throws Exception {

        assertEquals(expression, parser.toIntOperands("1+a"));
    }
}