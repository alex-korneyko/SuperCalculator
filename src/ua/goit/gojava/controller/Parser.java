package ua.goit.gojava.controller;

import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс преобразования строковых выражений в набор объектов,
 * используемых для дальнейших расчётов
 */
public class Parser implements Observable {

    //Список наблюдателей. Им будет разослано "сообщение", что Parser что-то сделал.
    //В этот список наблюдатели заносят сами себя из своих контекстов
    private List<Observer> observers = new ArrayList<>();

    //Сформированное выражение
    private List<ExpressionElement> expression = new ArrayList<>();

    /**
     * Преобразование строки с выражением в набор "математических" объектов,
     * который и будет служить для дальнейших приведений и расчётов
     *
     * @param stringExpression выражение в виде строки
     */
    public List<ExpressionElement> toIntOperands(String stringExpression) throws IllegalArgumentException {

        expression = new ArrayList<>();         //Выражение в виде набора мат-объектов

        char[] charExpression = stringExpression.toCharArray();
        boolean newNumber = true;               //флаг начала формирования очередного числа
        boolean negativeNumber = false;         //флаг отрицательного числа
        boolean dot = false;                    //Флаг десятичной точки

        for (char symbol : charExpression) {

            //формирование чисел
            if (symbol >= 48 && symbol <= 57) {
                if (newNumber) {
                    expression.add(new ExpressionElement(ElementType.INT, (symbol - 48)));
                    newNumber = false;
                } else {
                    if (dot) {
                        expression.get(expression.size() - 1).addDigitToFloatPart(symbol - 48);
                    } else {
                        expression.get(expression.size() - 1).addDigitToIntPart(symbol - 48);
                    }
                }
                continue;
            }
            //Найдена десятичная точка
            if(symbol == '.' || symbol == ','){
                dot = true;
                continue;
            }

            //определение мат. операций
            if (symbol == '+' || symbol == '-'
                    || symbol == '*' || symbol == '/') {
                if (newNumber) {
                    if (symbol == '-') {
                        negativeNumber = true;
                    }
                } else {
                    ExpressionElement tmp = expression.get(expression.size() - 1);
                    tmp.number = tmp.number * (negativeNumber ? -1 : 1);
                    negativeNumber = false;
                    newNumber = true;
                    dot = false;
                    expression.add(ExpressionElement.getExpressionElement(symbol));
                }
                continue;
            }

            //определение скобок
            if (symbol == '(') {
                expression.add(new ExpressionElement(ElementType.OPEN_PARENTHESIS));
                newNumber = true;
                continue;
            }

            if (symbol == ')') {
                ExpressionElement tmp = expression.get(expression.size() - 1);
                tmp.number = tmp.number * (negativeNumber ? -1 : 1);
                negativeNumber = false;
                expression.add(new ExpressionElement(ElementType.CLOSE_PARENTHESIS));
                continue;
            }

            throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }

        if ((expression.size() > 0
                && (expression.get(expression.size() - 1)).elementType == ElementType.INT
                && negativeNumber)) {
            expression.set(expression.size() - 1, new ExpressionElement(ElementType.INT, (expression.get(expression.size() - 1)).number * (-1)));
        }

        //Сообщение, что выражение составлено
        notifyObservers();

        return expression;
    }

    //region Event processing
    @Override
    public void registerObserver(Observer observer) {

        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {

        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {

        //В списке должно быть два наблюдателя: ParenthesesAddon и ConsoleDisplay.
        //Сообщить наблюдателям что выражение готово, но в обратном порядке списка,
        //т.к. надо сначала вывести распарсеное выражение (сообщение ConsoleDisplay)
        //а потом результат (сообщение ParenthesesAddon). Результат в конце расчётов окажется
        //вместо выражения. (При заполнении списка наблюдателей, первым в него попадает ParenthesesAddon,
        //который всё выражение сократит до результата)

        for (int i = observers.size() - 1; i >= 0; i--) {
            observers.get(i).update(expression);
        }
    }
    //endregion
}
