package paypals.exception;

public enum ExceptionMessage {
    INVALID_COMMAND("INPUT ERROR: Invalid command entered"),
    NO_DESCRIPTION("INPUT ERROR: No activity description"),
    NO_IDENTIFIER("INPUT ERROR: No identifier entered"),
    INVALID_IDENTIFIER("INPUT ERROR: Invalid identifier entered"),
    NO_PAYER("INPUT ERROR: No name of payer"),
    PAYER_OWES("LOGIC ERROR: Payer owes himself or herself"),
    DUPLICATE_FRIEND("LOGIC ERROR: Friend is mentioned twice in an activity"),
    INVALID_FRIEND("INPUT ERROR: Invalid friend entered"),
    ALREADY_PAID("LOGIC ERROR: Friend has already paid for this activity"),
    STORAGE_DIR_NOT_CREATED("ERROR: An error has occurred, storage directory not created"),
    STORAGE_FILE_NOT_CREATED("ERROR: An error has occurred, storage file not created"),
    STORAGE_FILE_NOT_FOUND("ERROR: An error has occurred, storage file not found"),
    SAVE_NOT_WRITTEN("ERROR: An error has occurred, storage file not written");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
