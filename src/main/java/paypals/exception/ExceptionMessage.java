package paypals.exception;

public enum ExceptionMessage {
    INVALID_COMMAND("INPUT ERROR: Invalid command entered"),
    NO_DESCRIPTION("INPUT ERROR: No activity description"),
    NO_IDENTIFIER("INPUT ERROR: No identifier entered"),
    INVALID_IDENTIFIER("INPUT ERROR: Invalid identifier entered"),
    OUTOFBOUNDS_IDENTIFIER("INPUT ERROR: Identifier entered is out of bounds"),
    NO_PAYER("INPUT ERROR: No name of payer"),
    PAYER_OWES("LOGIC ERROR: Payer owes himself or herself"),
    DUPLICATE_FRIEND("LOGIC ERROR: Friend is mentioned twice in an activity"),
    INVALID_FRIEND("INPUT ERROR: Invalid friend entered"),
    INVALID_AMOUNT("INPUT ERROR: Amount owed is not a number"),
    NO_AMOUNT_ENTERED("INPUT ERROR: Amount owed is not entered for 1 or more friends"),
    MULTIPLE_AMOUNTS_ENTERED("INPUT ERROR: Multiple amounts entered for 1 or more friends"),
    ALREADY_PAID("LOGIC ERROR: Friend has already paid for this activity"),
    STORAGE_DIR_NOT_CREATED("ERROR: An error has occurred, storage directory not created"),
    STORAGE_FILE_NOT_CREATED("ERROR: An error has occurred, storage file not created"),
    STORAGE_FILE_NOT_FOUND("ERROR: An error has occurred, storage file not found"),
    SAVE_NOT_WRITTEN("ERROR: An error has occurred, storage file not written"),
    LOAD_ERROR("ERROR: An error has occurred, save file not loaded");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
