package ua.goit.gojava.controller;

import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Parser implements Observable {

    private List<Observer> observers = new ArrayList<>();

    private List<ExpressionElement> expression = new ArrayList<>();


    /**
     * Преобразование строки с выражением в набор "математических" объектов,
     * который и будет служить для дальнейших приведений и расчётов
     *
     * @param stringExpression выражение в виде строки
     */
    public void toIntOperands(String stringExpression) {

        expression = new ArrayList<>();

        char[] charExpression = stringExpression.toCharArray();
        boolean newNumber = true;
        boolean negativeNumber = false;

        for (int i = 0; i < charExpression.length; i++) {
            if (((int) charExpression[i]) >= 48 && ((int) charExpression[i]) <= 57) {
                if (newNumber) {
                    expression.add(new ExpressionElement(ElementType.INT, (charExpression[i] - 48)));
                    newNumber = false;
                } else {
                    ExpressionElement tmp = expression.get(expression.size() - 1);
                    tmp.number = tmp.number * 10 + charExpression[i] - 48;
                }
                continue;
            }

            if (charExpression[i] == '+' || charExpression[i] == '-'
                    || charExpression[i] == '*' || charExpression[i] == '/') {
                if (!newNumber) {
                    ExpressionElement tmp = expression.get(expression.size() - 1);
                    tmp.number = tmp.number * (negativeNumber ? -1 : 1);
                    negativeNumber = false;
                    newNumber = true;
                    expression.add(ExpressionElement.getExpressionElement(charExpression[i]));
                } else {
                    if (charExpression[i] == '-') {
                        negativeNumber = true;
                    }
                }
                continue;
            }

            if (charExpression[i] == '(') {
                expression.add(new ExpressionElement(ElementType.OPEN_PARENTHESIS));
                newNumber = true;
            }

            if (charExpression[i] == ')') {
                ExpressionElement tmp = expression.get(expression.size() - 1);
                tmp.number = tmp.number * (negativeNumber ? -1 : 1);
                negativeNumber = false;
                //newNumber = true;
                expression.add(new ExpressionElement(ElementType.CLOSE_PARENTHESIS));
            }
        }

        //Сообщение, что выражение составлено
        notifyObservers();
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

        for (Observer observer : observers) {

            observer.update(expression);
        }
    }
    //endregion
}
