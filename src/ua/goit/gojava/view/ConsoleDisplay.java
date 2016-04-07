package ua.goit.gojava.view;

import ua.goit.gojava.controller.Parser;
import ua.goit.gojava.model.ParenthesesAddon;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;


public class ConsoleDisplay implements Observer {

    List<ExpressionElement> expression = new ArrayList<>();


    public ConsoleDisplay(Parser parser, ParenthesesAddon calculator) {
        parser.registerObserver(this);                      //Регистрация у наблюдаемого
        calculator.registerObserver(this);                  //Регистрация у наблюдаемого
        update(expression);                                 //Вывод приглашения на экран сразу при создании объекта
    }

    //Метод, вызываемый обработчиком исключений
    public void printError(String msg) {
        System.out.println(msg);
        System.out.print("Enter expression ('0' for exit) --> ");
    }

    @Override
    public void update(List<ExpressionElement> expression) {
        //Метод, вызываемый наблюдаемым объектом, чтобы ссобщить, что у него изменилось состояние
        //и передаёт изменения, а наблюдатель (этот объект) должен что-то сделать

        //Вывод на экран конечного выражения
        if (expression != null) {
            for (ExpressionElement element : expression) {
                System.out.print(element + " ");
            }
        }

        if (expression != null && expression.size() <= 2) {
            System.out.println();
            //Вывод на экран приглашения
            System.out.print("Enter expression ('0' for exit) --> ");
        }
    }
}
