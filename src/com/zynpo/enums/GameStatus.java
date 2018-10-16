package com.zynpo.enums;

public enum GameStatus {
    InPlay(0),
    WhiteInCheck(1),
    BlackInCheck(2),
    DrawByStalemate(3),
    DrawByInsufficientMaterial(4),
    DrawByRepetition(5),
    DrawByLackOfProgress(6),
    DrawByAgreement(7),
    WhiteWinByCheckmate(8),
    WhiteWinByTimeOut(9),
    WhiteWinByResignation(10),
    BlackWinByCheckmate(11),
    BlackWinByTimeOut(12),
    BlackWinByResignation(13);

    private int _value;

    GameStatus(int value) { _value = value; }

    public boolean meansGameIsOver() {
        switch (this) {
            case InPlay:
            case WhiteInCheck:
            case BlackInCheck:
                return false;
            default:
                return true;
        }
    }
}
