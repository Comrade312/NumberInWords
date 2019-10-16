package NumberInWords;

public class NumberLengthException extends RuntimeException {
    public NumberLengthException() {
        super("Слишкол длинное число");
    }

    public NumberLengthException(String message) {
        super(message);
    }
}
