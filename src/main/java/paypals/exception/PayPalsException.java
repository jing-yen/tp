package paypals.exception;

public class PayPalsException extends Exception {
    public PayPalsException(ExceptionMessage message) {
        super(message.getMessage());
    }
    public PayPalsException(ExceptionMessage message, String extraInfo) {
        super(message.getMessage() + extraInfo);
    }
}
