package exception;

public class MissingInputException extends Exception {
    public static final String DEFAULT_PERSON_MESSAGE = "First/Last name and Email is missing!";
    public static final String DEFAULT_HOBBY_MESSAGE = "Name is missing or is not unique!";
    public MissingInputException(String message) {
        super(message);
    }
}
