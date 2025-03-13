package paypals.exception;

public enum ExceptionMessage {
    INVALID_COMMAND("INPUT ERROR: Invalid command entered");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
