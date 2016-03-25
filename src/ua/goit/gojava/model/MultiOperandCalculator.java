package ua.goit.gojava.model;

import ua.goit.gojava.controller.Parser;
import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;

public class MultiOperandCalculator extends Decorator implements Observer, Observable {

    List<Observer> observers = new ArrayList<>();

    List<ExpressionElement> expression = new ArrayList<>();

    //Конструктор, если не предусматривается наблюдение
    public MultiOperandCalculator(DecoratorInterface decoratorInterface){

        //Вызов конструктора декорируемого класса
        super(decoratorInterface);
    }

    //Конструктор, если надо кого-то наблюдать
    public MultiOperandCalculator(DecoratorInterface decoratorInterface, Parser parser) {

        //Вызов конструктора декорируемого класса
        this(decoratorInterface);

        parser.registerObserver(this);
    }

    @Override
    public int compute(List<ExpressionElement> expression) {

        //Передача управления декорируемому классу
        return super.compute(expression);
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

    @Override
    public void update(List<ExpressionElement> expression) {
        this.expression = expression;

        int result = compute(expression);

        expression.add(new ExpressionElement(ElementType.EQUALLY));
        expression.add(new ExpressionElement(ElementType.INT, result));

        notifyObservers();
    }
}
