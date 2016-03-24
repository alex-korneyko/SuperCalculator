package ua.goit.gojava.model;

import ua.goit.gojava.controller.Parser;
import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;


public class SimpleCalculator implements Observer, Observable {

    Parser parser;

    List<Observer> observers = new ArrayList<>();

    public SimpleCalculator(Parser parser) {
        this.parser = parser;
        parser.registerObserver(this);
    }

    List<ExpressionElement> expressionElements = new ArrayList<>();

    @Override
    public void update(List<ExpressionElement> elements) {
        expressionElements = elements;
        int result = compute(expressionElements);
        expressionElements.add(new ExpressionElement(ElementType.EQUALLY));
        expressionElements.add(new ExpressionElement(ElementType.INT, result));

        notifyObservers();
    }

    public int compute(List<ExpressionElement> elements) {
        int operand1 = 0;
        int operand2 = 0;

        int result = 0;

        if (elements.get(0).elementType == ElementType.INT) {
            operand1 = elements.get(0).number;
        }

        if (elements.get(2).elementType == ElementType.INT) {
            operand2 = elements.get(2).number;
        }

        switch (elements.get(1).elementType) {
            case PLUS:
                result = operand1 + operand2;
                break;
            case MINUS:
                result = operand1 - operand2;
                break;
            case MULTIPLY:
                result = operand1 * operand2;
                break;
            case DIVIDE:
                result = operand1 / operand2;
                break;
        }

        return result;
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
        for(Observer o: observers){
            o.update(expressionElements);
        }
    }
}