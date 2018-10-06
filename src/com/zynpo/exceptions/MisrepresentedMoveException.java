package com.zynpo.exceptions;

public class MisrepresentedMoveException  extends MoveException {
    public MisrepresentedMoveException() {
        super();
    }

    public MisrepresentedMoveException(String message) {
        super(message);
    }

    public MisrepresentedMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public MisrepresentedMoveException(Throwable cause) {
        super(cause);
    }
}
