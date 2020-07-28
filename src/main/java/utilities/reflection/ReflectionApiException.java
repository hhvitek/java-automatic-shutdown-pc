package utilities.reflection;

public class ReflectionApiException extends RuntimeException {


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
