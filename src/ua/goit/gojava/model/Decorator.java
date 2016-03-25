package ua.goit.gojava.model;


import ua.goit.gojava.servicePackage.ExpressionElement;

import java.util.List;

/**
 * Класс-декоратор
 */
public abstract class Decorator implements DecoratorInterface {

    protected DecoratorInterface decoratorInterface;

    public Decorator(DecoratorInterface decoratorInterface){
        this.decoratorInterface = decoratorInterface;
    }

    @Override
    public int compute(List<ExpressionElement> expression) {

        return decoratorInterface.compute(expression);
    }
}
