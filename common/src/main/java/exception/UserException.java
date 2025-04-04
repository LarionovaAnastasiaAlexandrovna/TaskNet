package exception;

public class UserException extends RuntimeException {
    public enum Type {
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        INVALID_PASSWORD
    }

    private final Type type;

    public UserException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
