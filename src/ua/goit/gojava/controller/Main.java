package ua.goit.gojava.controller;


import ua.goit.gojava.model.Decorator;
import ua.goit.gojava.model.MultiOperandCalculator;
import ua.goit.gojava.model.SimpleCalculator;
import ua.goit.gojava.model.TrickyCalculator;
import ua.goit.gojava.view.ConsoleDisplay;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser();

        //Создание объекта-декоратора, в котором расширяется функциональность SimpleCalculator
        //с помощью класса MultiOperandCalculator,
        //а потом то что получилось расширяется классом TrickyCalculator
        Decorator calculator = new TrickyCalculator(new MultiOperandCalculator(new SimpleCalculator()), parser);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(parser, (TrickyCalculator) calculator);

        while (true) {
            String s = Input.keyboard();

            if(Objects.equals(s, "0")){
                break;
            }


            try {
                parser.toIntOperands(s);
            } catch (IllegalArgumentException e) {
                consoleDisplay.printError(e.getMessage());
            }
        }
    }
}
