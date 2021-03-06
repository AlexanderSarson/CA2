package exception;

public class MissingInputException extends Exception {
    public static final String DEFAULT_PERSON_MESSAGE = "First/Last name and Email is missing!";
    public static final String DEFAULT_HOBBY_MESSAGE = "Name is missing or is not unique!";
    public static final String DEFAULT_PHONE_MESSAGE = "Number is missing or is not unique!";
    public static final String DEFAULT_CITYINFO_MESSAGE = "ZipCode/City is missing or ZipCode is not unique!";
    public static final String DEFAULT_ADDRESS_MESSAGE = "Street is missing!";
    public MissingInputException(String message) {
        super(message);
    }
}
