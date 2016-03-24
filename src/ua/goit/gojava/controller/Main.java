package ua.goit.gojava.controller;


import ua.goit.gojava.model.SimpleCalculator;
import ua.goit.gojava.view.ConsoleDisplay;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser();

        SimpleCalculator simpleCalculator = new SimpleCalculator(parser);

        ConsoleDisplay display = new ConsoleDisplay(simpleCalculator);

        while (true) {
            String s = Input.keyboard();

            if(Objects.equals(s, "0")){
                break;
            }

            parser.toIntOperands(s);
        }
    }
}
