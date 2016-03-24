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
            case OPEN_BRACKET:
                return "(";
            case CLOSE_BRACKET:
                return ")";
            default:
                return "";
        }
    }
}
