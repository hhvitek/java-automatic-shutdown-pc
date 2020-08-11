package utilities.reflection;

public class ReflectionApiException extends RuntimeException {

    private static final long serialVersionUID = -3834093847408469388L;

    public ReflectionApiException(String message) {
        super(message);
    }

    public ReflectionApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionApiException(Throwable cause) {
        super(cause);
    }
}
