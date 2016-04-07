package ua.goit.gojava.controller;


import ua.goit.gojava.model.Decorator;
import ua.goit.gojava.model.MultiOperandAddon;
import ua.goit.gojava.model.ParenthesesAddon;
import ua.goit.gojava.model.SimpleCalculator;
import ua.goit.gojava.view.ConsoleDisplay;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        //------------ Часть CONTROLLER паттерна MVC -----------
        //Объект - парсер. Преобразование строки в выражение для расчётов
        Parser parser = new Parser();

        //-------------- Часть MODEL паттерна MVC --------------
        //Создание объекта-декоратора (паттерн DECORATOR), в котором расширяется функциональность SimpleCalculator
        //с помощью класса MultiOperandAddon,
        //а потом то что получилось расширяется классом ParenthesesAddon.
        //SimpleCalculator принимает в качестве аргумента объект,
        //который он должен прослушивать - parser. (паттерн OBSERVER)
        Decorator calculator = new ParenthesesAddon(new MultiOperandAddon(new SimpleCalculator()), parser);

        //--------------- Часть VIEW паттерна MVC ---------------
        //Объект для вывода результатов на экран. Принимает аргументами объекты,
        //которые должен "прослушивать", т.е. ловить от них события (паттерн OBSERVER)
        ConsoleDisplay consoleDisplay = new ConsoleDisplay(parser, (ParenthesesAddon) calculator);

        //Ввод с клавиатуры
        while (true) {
            String s = Input.keyboard();

            if (Objects.equals(s, "0")) {
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
