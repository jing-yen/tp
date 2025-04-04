package paypals.exception;

public class PayPalsException extends Exception {

    /**
     * Constructs a new {@code PayPalsException} with the specified error message
     * retrieved from the {@link ExceptionMessage} enum.
     *
     * @param message an instance of {@link ExceptionMessage} containing the predefined error message
     */
    public PayPalsException(ExceptionMessage message) {
        super(message.getMessage());
    }

    /**
     * Constructs a new {@code PayPalsException} with a combination of the predefined error message
     * from the {@link ExceptionMessage} enum and additional contextual information.
     *
     * @param message   an instance of {@link ExceptionMessage} containing the predefined error message
     * @param extraInfo a string providing additional context or details about the error
     */
    public PayPalsException(ExceptionMessage message, String extraInfo) {
        super(message.getMessage() + extraInfo);
    }
}
