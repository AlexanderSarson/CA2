package exception;

public class CityInfoNotFoundException extends Exception {
    public static final String DEFAULT_CITYINFO_MESSAGE = "CityInfo Not Found!";
    public CityInfoNotFoundException() {
        super(DEFAULT_CITYINFO_MESSAGE);
    }
    public CityInfoNotFoundException(String message) {
        super(message);
    }
}
