package exception;

public class PersonNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE ="Person Not Found!";

    public PersonNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
