package ua.goit.gojava.model;

import ua.goit.gojava.servicePackage.ExpressionElement;

import java.util.List;


public interface DecoratorInterface {

    double compute(List<ExpressionElement> expression);
}
