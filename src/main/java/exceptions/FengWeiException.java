package exceptions;

/**
 * Custom exception class for FengWei application errors.
 */
public class FengWeiException extends Exception {
    public FengWeiException(String message) {
        super(message);
    }
}
