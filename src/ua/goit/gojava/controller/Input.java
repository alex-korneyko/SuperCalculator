package ua.goit.gojava.controller;

import java.util.Scanner;

public class Input {

    public static String keyboard() {

        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();


    }
}
