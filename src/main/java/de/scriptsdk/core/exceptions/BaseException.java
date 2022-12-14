package de.scriptsdk.core.exceptions;

/**
 * @author Crome696
 * @version 1.0
 */
public class BaseException extends RuntimeException {

    public BaseException(String format, Object... args) {
        this(String.format(format, args));
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}