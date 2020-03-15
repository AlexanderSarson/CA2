package exception;

public class MissingInputException extends Exception {
    public static final String DEFAUL_PERSON_MESSAGE = "First/Last name and Email is missing";
    public MissingInputException(String message) {
        super(message);
    }
}
