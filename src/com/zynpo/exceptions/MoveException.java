package com.zynpo.exceptions;

public abstract class MoveException extends Exception {
    public MoveException() {
        super();
    }

    public MoveException(String message) {
        super(message);
    }

    public MoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoveException(Throwable cause) {
        super(cause);
    }
}
