package com.zynpo.enums;

public enum GameStatus {
    NotDetermined(-1),
    InPlay(0),
    DrawByStalemate(1),
    DrawByInsufficientMaterial(2),
    DrawByRepetition(3),
    DrawByLackOfProgress(4),
    DrawByAgreement(5),
    WhiteWinByCheckmate(6),
    WhiteWinByTimeOut(7),
    WhiteWinByResignation(8),
    BlackWinByCheckmate(9),
    BlackWinByTimeOut(10),
    BlackWinByResignation(11);

    private int _value;

    GameStatus(int value) { this._value = value; }
}
