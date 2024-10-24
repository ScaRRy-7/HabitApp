package habitapp.exceptions;

public class UserException extends RuntimeException {
    private final int errorCode;

    public UserException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
