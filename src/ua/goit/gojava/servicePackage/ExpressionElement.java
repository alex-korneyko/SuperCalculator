package ua.goit.gojava.servicePackage;


public class ExpressionElement {

    public ElementType elementType;

    public int number;

    public ExpressionElement(ElementType elementType) {
        this.elementType = elementType;
    }

    public ExpressionElement(ElementType elementType, int number) {
        this(elementType);
        this.number = number;
    }

    public ElementType getSpecies() {
        if (elementType == ElementType.PLUS || elementType == ElementType.MINUS
                || elementType == ElementType.MULTIPLY || elementType == ElementType.DIVIDE) {
            return ElementType.OPERATOR;
        }

        if (elementType == ElementType.OPEN_PARENTHESIS || elementType == ElementType.CLOSE_PARENTHESIS) {
            return ElementType.PARENTHESIS;
        }

        return ElementType.INT;
    }

    public static ExpressionElement getExpressionElement(char symbol){
        switch (symbol){
            case '+':
                return new ExpressionElement(ElementType.PLUS);
            case '-':
                return new ExpressionElement(ElementType.MINUS);
            case '*':
                return new ExpressionElement(ElementType.MULTIPLY);
            case '/':
                return new ExpressionElement(ElementType.DIVIDE);
        }

        throw new IllegalArgumentException("Incorrect operator");
    }

    @Override
    public String toString() {
        if (elementType == ElementType.INT) {
            return String.valueOf(number);
        }

        switch (elementType) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case DIVIDE:
                return "/";
            case MULTIPLY:
                return "*";
            case EQUALLY:
                return "=";
            case OPEN_PARENTHESIS:
                return "(";
            case CLOSE_PARENTHESIS:
                return ")";
            default:
                return "";
        }
    }

    @Override
    public ExpressionElement clone() {

        return new ExpressionElement(this.elementType, this.number);
    }

    public boolean equals(Object element) {
        return this.elementType == ((ExpressionElement) element).elementType
                && number == ((ExpressionElement) element).number;
    }


}
