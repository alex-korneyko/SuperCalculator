package ua.goit.gojava.model;


import ua.goit.gojava.controller.Parser;
import ua.goit.gojava.servicePackage.ElementType;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observable;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, добавляющий функционал по расчёту выражений со скобками
 */
public class TrickyCalculator extends Decorator implements Observer, Observable {

    List<Observer> observers = new ArrayList<>();

    List<ExpressionElement> expression = new ArrayList<>();

    public TrickyCalculator(DecoratorInterface decoratorInterface, Parser parser) {

        //Вызов конструктора декорируемого класса
        super(decoratorInterface);

        //Регистрация у наблюдаемого
        parser.registerObserver(this);
    }

    /**
     * Метод, раскрывает все скобки в выражении и передаёт его методу работающему
     * с любым количеством операндов
     *
     * @param expression выражение для преобразования
     * @return result of the expression
     */
    @Override
    public int compute(List<ExpressionElement> expression) {
        //Раскрытие всех скобок в выражении

        List<ExpressionElement> simpleExpression = new ArrayList<>();

        int dynamicExprSize = expression.size();
        for (int i = 0; i < dynamicExprSize; i++) {
            if (expression.get(i).elementType == ElementType.CLOSE_PARENTHESIS) {
                for (int j = i - 1; j >= 0; j--) {
                    if (expression.get(j).elementType != ElementType.OPEN_PARENTHESIS) {
                        simpleExpression.add(0, expression.get(j));
                    } else {
                        final int simpleResult = super.compute(simpleExpression);
                        expression.set(j, new ExpressionElement(ElementType.INT, simpleResult));
                        for (int k = j + 1; k <= i; k++) {
                            expression.remove(j + 1);
                            --dynamicExprSize;
                        }
                        i = j;
                        simpleExpression.clear();
                        break;
                    }
                }
            }
        }

        //Передача управления декорируемому классу
        return super.compute(expression);
    }

    //region Event precessing
    @Override
    public void update(List<ExpressionElement> expression) {
        //Метод, вызываемый наблюдаемым, чтобы ссобщить, что у него изменилось состояние
        //и передаёт изменения, а наблюдатель (этот объект) должен что-то сделать

        this.expression = expression;

        //Расчёт
        int result = compute(expression);

        //Добавление результата в выражение в виде мат-объектов
        expression.add(new ExpressionElement(ElementType.EQUALLY));
        expression.add(new ExpressionElement(ElementType.INT, result));

        //Сообщение всем наблюдателям об изменении состояния выражения
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
        for (Observer observer : observers) {
            observer.update(expression);
        }
    }
    //endregion
}
