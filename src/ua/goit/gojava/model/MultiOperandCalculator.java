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
     * Принцип такой - в выражении сначала берётся первые два операнда и операция
     * между ними, а потом, если это низкоприоритетная операция, ищется
     * высокоприоритетная операция, и если находится, то берутся операнды
     * этой операции. Далее найденые два операнда и операция передаются более простому
     * классу, который работает только с двумя операндами. Возвращённый результат
     * записывается в главное выражение вместо вычисленых двух операндов и операции
     * между ними. Так продолжается, пока главное выражение не сократится до
     * двух операндов и операции. Потом и оно передаётся в следующий, декорируемый,
     * класс, который и посчитает уже конечный результат.
     * Пример: a+b*c+d, алгоритм находит b*c и передаёт следующему алгоритму,
     * тот возвращает результат r, и он вставляется в главное выражение вместо b*c,
     * т.е. получаем a+r+d. Потом передаётся уже a+r, результат r2 вставляется в
     * главное выражение: r2+d, и наконец это выражение передаётся следующему алгоритму
     * и получается конечный результат
     *
     * @param expression выражение для вычесления (коллекция объектов выражения)
     * @return result of the expression
     */
    @Override
    public int compute(List<ExpressionElement> expression) {

        //Простое выражение из двух операндов
        List<ExpressionElement> simpleExpression;

        //Повторение, пока длина главного выражения больше 3-ёх
        while (expression.size() > 3) {

            int startIndexSimpleExp = 0;
            //Помещение в простое выражение первых двух операндов из главного выражения и операция между ними
            simpleExpression = expression.subList(0, 3);

            //Поиск более приоритетных операций в главном выражении начиная с четвёртого элемента
            for (int i = 3; i < expression.size(); i++) {

                //Если текущая операция в простом выражении плюс или минус, то ищем более приоритетные операции
                if (simpleExpression.get(1).elementType == ElementType.PLUS ||
                        simpleExpression.get(1).elementType == ElementType.MINUS) {

                    //Если найдена более приоритетная операция, то она заносится в простое выражение
                    //и операнды этой операции тоже
                    if (expression.get(i).elementType == ElementType.MULTIPLY ||
                            expression.get(i).elementType == ElementType.DIVIDE) {

                        simpleExpression = expression.subList(i - 1, i + 2);
                        //запоминание начал простого выражения в главном выражении
                        startIndexSimpleExp = i - 1;
                        //прекращение цикла, т.к. дальше искать приоритетные операции нет смысла
                        break;
                    }
                }
            }
            //Передача простого выражения в декорируемый класс, который работает только с двумя операндами
            int result = super.compute(simpleExpression);

            //Замена простого выражения в главном выражении результатм вычисления этого простого выражения
            expression.set(startIndexSimpleExp, new ExpressionElement(ElementType.INT, result));
            expression.remove(startIndexSimpleExp + 1);
            expression.remove(startIndexSimpleExp + 1);
        }

        //Когда длина главного выражения стала меньше или равна 3,
        // то передача управления декорируемому классу
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
