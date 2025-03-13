package paypals.exception;

public class PayPalsException extends Exception {
    public PayPalsException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
