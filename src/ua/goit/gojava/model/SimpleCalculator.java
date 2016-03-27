package ua.goit.gojava.model;

import ua.goit.gojava.controller.Parser;
import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Кдасс, расчитывающий выражение из двух операторов и простейших
 * арифметических опреций
 */
public class SimpleCalculator implements Observer, Observable, DecoratorInterface {

    Parser parser;

    List<Observer> observers = new ArrayList<>();

    List<ExpressionElement> expressionElements = new ArrayList<>();

    //Конструктор, если надо что-то брать под наблюдение
    public SimpleCalculator(Parser parser) {
        this.parser = parser;

        parser.registerObserver(this);
    }

    //Конструктор, если не предусматривается наблюдение
    public SimpleCalculator(){}

    /**
     * Метод производящий только простые арифметические операции ('+', '-', '*', '/'
     * с двумя операндами
     * @param expression  expression for calculate
     * @return result of the expression
     */
    @Override
    public int compute(List<ExpressionElement> expression) {
        int operand1 = 0;
        int operand2 = 0;
        int result = 0;

        if(expression.size() == 1){
            return expression.get(0).number;
        }

        if (expression.get(0).elementType == ElementType.INT) {
            operand1 = expression.get(0).number;
        }

        if (expression.get(2).elementType == ElementType.INT) {
            operand2 = expression.get(2).number;
        }

        switch (expression.get(1).elementType) {
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


    //region Event processing
    @Override
    public void update(List<ExpressionElement> expression) {
        //Метод, вызываемый наблюдаемым, чтобы ссобщить, что у него изменилось состояние
        //и передаёт изменения, а наблюдатель (этот объект) должен что-то сделать

        //expressionElements = expression;

        //производится расчёт простейшим калькулятором
        int result = compute(expression);

        //дописывание результата в выражение

        expression.add(new ExpressionElement(ElementType.EQUALLY));
        expression.add(new ExpressionElement(ElementType.INT, result));

        //сообщение всем наблюдателям о изменении состояния
        notifyObservers();
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
        for (Observer o : observers) {
            o.update(expressionElements);
        }
    }
    //endregion
}