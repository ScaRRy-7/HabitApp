package habitapp.exceptions;

public class UserIllegalRequestException extends RuntimeException {
    private final int errorCode;

    public UserIllegalRequestException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
