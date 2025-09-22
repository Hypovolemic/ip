package exceptions;

/**
 * Represents a custom exception for the FengWei application.
 * This exception is thrown when there are errors specific to FengWei operations,
 * such as invalid user input, command parsing errors, or task management issues.
 */
public class FengWeiException extends Exception {

    /**
     * Constructs a new FengWeiException with the specified error message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public FengWeiException(String message) {
        super(message);
    }

    /**
     * Constructs a new FengWeiException with the specified error message and cause.
     *
     * @param message the detail message explaining the cause of the exception
     * @param cause the cause of the exception (which is saved for later retrieval)
     */
    public FengWeiException(String message, Throwable cause) {
        super(message, cause);
    }
}
