package com.zynpo.exceptions;

public class AmbiguousMoveException extends MoveException {
    public AmbiguousMoveException() {
        super();
    }

    public AmbiguousMoveException(String message) {
        super(message);
    }

    public AmbiguousMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmbiguousMoveException(Throwable cause) {
        super(cause);
    }
}