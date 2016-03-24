package ua.goit.gojava.controller;


import ua.goit.gojava.model.SimpleCalculator;
import ua.goit.gojava.view.Display;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser();

        SimpleCalculator simpleCalculator = new SimpleCalculator(parser);

        Display display = new Display(simpleCalculator);

        Scanner scanner = new Scanner(System.in);

        String a = "";

        while (true) {
            a = scanner.next();
            if (Objects.equals(a, "0")) {
                break;
            }
            parser.toIntOperands(a);
        }

    }
}
