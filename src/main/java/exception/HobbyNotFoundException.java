package exception;


public class HobbyNotFoundException extends Exception {
    public static final String DEFAULT_HOBBY_MESSAGE = "Hobby Not Found!";
    public HobbyNotFoundException() {
        super(DEFAULT_HOBBY_MESSAGE);
    }
    public HobbyNotFoundException(String message) {
        super(message);
    }
}
