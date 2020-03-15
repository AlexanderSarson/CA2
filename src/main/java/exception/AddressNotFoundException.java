package exception;

public class AddressNotFoundException extends Exception{
    public static final String DEFAULT_ADDRESS_MESSAGE = "Address Not Found!";
    public AddressNotFoundException() {
        super(DEFAULT_ADDRESS_MESSAGE);
    }
    public AddressNotFoundException(String message) {
        super(message);
    }
}
