package utilities.reflection;

public class ClassLoadingException extends ReflectiveOperationException {

    private static final long serialVersionUID = 8548470707517885021L;

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
