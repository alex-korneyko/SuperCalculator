package ua.goit.gojava.model;

import ua.goit.gojava.controller.Parser;
import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, добавляющий функционал по расчёту выражений с числом
 * операндов больше чем два
 */
public class MultiOperandCalculator extends Decorator implements Observer, Observable {

    List<Observer> observers = new ArrayList<>();

    List<ExpressionElement> expression = new ArrayList<>();

    //Конструктор, если не предусматривается наблюдение
    public MultiOperandCalculator(DecoratorInterface decoratorInterface) {

        //Вызов конструктора декорируемого класса
        super(decoratorInterface);
    }

    //Конструктор, если надо кого-то наблюдать
    public MultiOperandCalculator(DecoratorInterface decoratorInterface, Parser parser) {

        //Вызов конструктора декорируемого класса
        this(decoratorInterface);

        parser.registerObserver(this);
    }

    /**
     * Метод, определяющий приоритетность операций в выражениии
     * и передающий операнды парами в метод работающий только с парами операндов
     *
     * @param expression выражение для вычесления (коллекция объектов выражения)
     * @return result of the expression
     */
    @Override
    public int compute(List<ExpressionElement> expression) {

        List<ExpressionElement> simpleExpression;

        while (expression.size() > 3) {

            int startIndexSimpleExp = 0;
            simpleExpression = expression.subList(0, 3);

            for (int i = 3; i < expression.size(); i++) {


                if (simpleExpression.get(1).elementType == ElementType.PLUS ||
                        simpleExpression.get(1).elementType == ElementType.MINUS) {

                    if (expression.get(i).elementType == ElementType.MULTIPLY ||
                            expression.get(i).elementType == ElementType.DIVIDE) {

                        simpleExpression = expression.subList(i - 1, i + 2);
                        startIndexSimpleExp = i - 1;
                        break;
                    }
                }
            }
            int result = super.compute(simpleExpression);
            expression.set(startIndexSimpleExp, new ExpressionElement(ElementType.INT, result));
            expression.remove(startIndexSimpleExp + 1);
            expression.remove(startIndexSimpleExp + 1);
        }

        //Передача управления декорируемому классу
        return super.compute(expression);
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

    @Override
    public void update(List<ExpressionElement> expression) {
        //Метод, вызываемый наблюдаемым, чтобы ссобщить, что у него изменилось состояние
        //и передаёт изменения, а наблюдатель (этот объект) должен что-то сделать

        this.expression = expression;

        //Производится расчёт выражения с множеством операндов, но без скобок
        int result = compute(expression);

        //дописывание результата в выражение
        expression.add(new ExpressionElement(ElementType.EQUALLY));
        expression.add(new ExpressionElement(ElementType.INT, result));

        //сообщение всем наблюдателям о изменении состояния
        notifyObservers();
    }
    //endregion
}
