package ua.goit.gojava.view;

import ua.goit.gojava.model.SimpleCalculator;
import ua.goit.gojava.servicePackage.ExpressionElement;
import ua.goit.gojava.servicePackage.Observer;

import java.util.ArrayList;
import java.util.List;


public class ConsoleDisplay implements Observer {

    List<ExpressionElement> elements = new ArrayList<>();


    public ConsoleDisplay(SimpleCalculator calculator){
        calculator.registerObserver(this);
        update(elements);
    }

    @Override
    public void update(List<ExpressionElement> expression) {
        for (ExpressionElement element: expression){
            System.out.print(element + " ");
        }
        System.out.println();

        System.out.print("Enter expression ('0' for exit) --> ");
    }
}
