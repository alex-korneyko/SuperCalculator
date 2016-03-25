package ua.goit.gojava.controller;

import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;


public class Parser implements Observable {

    private List<Observer> observers;

    private List<ExpressionElement> expression = new ArrayList<>();

    public Parser() {
        observers = new ArrayList<>();
    }

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

    public void toIntOperands(String stringExpression) {

        expression = new ArrayList<>();
        int number = 0;
        boolean newNumber = true;
        boolean negativeNumber = false;

        char[] chars = stringExpression.toCharArray();

        for (char aChar : chars) {

            if (((int) aChar) >= 48 && ((int) aChar) <= 57) {
                number = number * 10 + (((int) aChar) - 48);
                newNumber = false;
                continue;
            }

            if (aChar == '+' || aChar == '-' || aChar == '*' || aChar == '/') {

                if (!newNumber) {
                    number = number * (negativeNumber ? -1 : 1);
                    negativeNumber = false;

                    expression.add(new ExpressionElement(ElementType.INT, number));

                    number = 0;
                    newNumber = true;

                    switch (aChar) {
                        case '+':
                            expression.add(new ExpressionElement(ElementType.PLUS));
                            break;
                        case '-':
                            expression.add(new ExpressionElement(ElementType.MINUS));
                            break;
                        case '*':
                            expression.add(new ExpressionElement(ElementType.MULTIPLY));
                            break;
                        case '/':
                            expression.add(new ExpressionElement(ElementType.DIVIDE));
                            break;
                        case '(':
                            expression.add(new ExpressionElement(ElementType.OPEN_PARENTHESIS));
                            break;
                        case ')':
                            expression.add(new ExpressionElement(ElementType.CLOSE_PARENTHESIS));

                    }
                } else {
                    if (aChar == '-') {
                        negativeNumber = true;
                    }
                }
            }
        }
        number = number * (negativeNumber ? -1 : 1);
        expression.add(new ExpressionElement(ElementType.INT, number));

        if (expression.size() == 3 && expression.get(0).elementType == ElementType.INT
                && expression.get(2).elementType == ElementType.INT
                && expression.get(1).elementType != ElementType.INT) {

            notifyObservers();
        }
    }
}
