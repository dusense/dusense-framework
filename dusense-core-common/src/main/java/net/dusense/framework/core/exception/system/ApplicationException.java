package net.dusense.framework.core.exception.system;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 3583566093089790852L;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
