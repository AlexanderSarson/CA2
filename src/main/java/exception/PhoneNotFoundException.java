package exception;

import entities.Phone;

public class PhoneNotFoundException extends Exception {
    public static final String DEFAULT_PHONE_MESSAGE = "Phone Not Found!";
    public PhoneNotFoundException() {
        super(DEFAULT_PHONE_MESSAGE);
    }
    public PhoneNotFoundException(String message) {
        super(message);
    }

}
