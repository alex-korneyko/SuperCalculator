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
public class ParenthesesAddon extends Decorator implements Observer, Observable {

    List<Observer> observers = new ArrayList<>();       //Список наблюдателей

    List<ExpressionElement> expression = new ArrayList<>();     //Выражение с которым будет работать вся программа

    public ParenthesesAddon(DecoratorInterface decoratorInterface, Parser parser) {

        //Вызов конструктора декорируемого класса
        super(decoratorInterface);

        //Регистрация у наблюдаемого
        parser.registerObserver(this);
    }

    /**
     * Метод, раскрывает все скобки в выражении и передаёт это выражение
     * методу, работающему с любым количеством операндов
     * Принцип такой - в выражении ищется первая закрывающая скобка,
     * потом в обратном напрвлении ищется открывающая скобка. В результате
     * получается выражение между скобками, которое может иметь любое количество операндов
     * и простые математические операции. Это выражение передаётся классу, который
     * декорирован этим классом, т.е. более простому, работающему с выражениями без скобок.
     * Возвращаемое значение заносится в выражение на место найденной открывающей скобки,
     * а всё остальное до закрывающей скобки, и включая саму скобку удаляется.
     * Т.е. в результате вместо выражения в скобках вставляется его результат
     * Пример: a+(b+c+d)+e, алгоритм находит b+c+d, предаёт это выражение следующему алгоритму,
     * тот возвращает результат r, и он вставляется вместо (b+c+d), т.е. получаем a+r+e.
     * Потом опять поиск скобок, если больше скобок нет, то работа передаётся следующему алгоритму.
     *
     * @param expression выражение для преобразования
     * @return result of the expression
     */
    @Override
    public int compute(List<ExpressionElement> expression) throws IllegalArgumentException {
        //Раскрытие всех скобок в выражении

        //"Простое" выражение, т.е. без скобок. Оно будет пердаваться следующему алгоритму
        List<ExpressionElement> simpleExpression = new ArrayList<>();

        //Динамический размер главного выражения, он будет изменятся по мере замены
        //выражений в скобках их результатом
        int dynamicExprSize = expression.size();

        //Поиск первой ЗАКРЫВАЮЩЕЙ скобки
        for (int i = 0; i < dynamicExprSize; i++) {
            if (expression.get(i).elementType == ElementType.CLOSE_PARENTHESIS) {
                //Поиск "назад" ОТКРЫВАЮЩЕЙ скобки (которая соответствует найденной закрывающей)
                for (int j = i - 1; j >= 0; j--) {
                    if (expression.get(j).elementType != ElementType.OPEN_PARENTHESIS) {
                        //Если дошли до начала выражения, а открывающая не найдена, то исключение
                        if (j == 0) {
                            expression.clear();
                            throw new IllegalArgumentException("<-- To many closing parentheses!");
                        }
                        //Если открывающая всё ещё не найдена, то заполнение "простого" выражения
                        //Элементы всё время вставляются на первое место, но т.к. в текущем цикле
                        //движение производится назад, то "простое" формируется в правильном порядке
                        simpleExpression.add(0, expression.get(j));
                    } else {
                        //когда найдена открывающая скобка, то "простое" выражение передаётся
                        //более простому алгоритму, который декорирован этим алгоритмом
                        final int simpleResult = super.compute(simpleExpression);
                        //результат записывается на место найденной ОТКРЫВАЮЩЕЙ скобки
                        expression.set(j, new ExpressionElement(ElementType.INT, simpleResult));
                        //всё остальное, включая найденную ЗАКРЫВАЮЩУЮ скобку удаляется
                        //а также соответственно уменьшается значение динамического размера выражения
                        for (int k = j + 1; k <= i; k++) {
                            expression.remove(j + 1);
                            --dynamicExprSize;
                        }
                        //индексу первого цикла (который ищет закрывающие) присваивается индекс второго цикла,
                        //по которому записан результат вычисления. И поиск ЗАКРЫВАЮЩЕЙ продолжится со
                        //следующего элемента в первом цикле
                        i = j;
                        simpleExpression.clear();
                        break;
                    }
                }
            }
        }

        //После циклов поиска не должно остатся ни одной открывающей, но если всё же
        //остались, то исключение
        if (expression.contains(new ExpressionElement(ElementType.OPEN_PARENTHESIS))) {
            expression.clear();
            throw new IllegalArgumentException("<-- To many opening parentheses!");
        }

        //Выражение уже без скобок
        //Передача управления декорируемому классу
        return super.compute(expression);
    }

    //region Event processing
    @Override
    public void update(List<ExpressionElement> expression) {
        //Метод, вызываемый наблюдаемым, чтобы ссобщить, что у него изменилось состояние
        //и передаёт изменения, а наблюдатель (этот объект) должен что-то сделать

        this.expression = expression;

        //Расчёт
        int result = compute(expression);

        //Добавление результата в выражение в виде мат-объектов
        expression.clear();
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
