package model;

public class ClassLoadingException extends ReflectiveOperationException {

    public ClassLoadingException(String message) {
        super(message);
    }

    public ClassLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassLoadingException(Throwable cause) {
        super(cause);
    }
}
