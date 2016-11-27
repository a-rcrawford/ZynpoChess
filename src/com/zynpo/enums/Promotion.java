package com.zynpo.enums;

public enum Promotion {
    NotApplicable(-1),
    ToQueen(0),
    ToCastle(1),
    ToKnight(2),
    ToBishop(3);

    private int _value;

    Promotion(int value) {
        this._value = value;
    }
}
