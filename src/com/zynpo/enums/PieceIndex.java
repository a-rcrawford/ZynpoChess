package com.zynpo.enums;


public enum PieceIndex {
    None(-1),
    WhiteLeftCastle(0),
    WhiteLeftKnight(1),
    WhiteLeftBishop(2),
    WhiteQueen(3),
    WhiteKing(4),
    WhiteRightBishop(5),
    WhiteRightKnight(6),
    WhiteRightCastle(7),

    WhitePawnA(8),
    WhitePawnB(9),
    WhitePawnC(10),
    WhitePawnD(11),
    WhitePawnE(12),
    WhitePawnF(13),
    WhitePawnG(14),
    WhitePawnH(15),

    BlackPawnA(15),
    BlackPawnB(17),
    BlackPawnC(18),
    BlackPawnD(19),
    BlackPawnE(20),
    BlackPawnF(21),
    BlackPawnG(22),
    BlackPawnH(23),

    BlackLeftCastle(24),
    BlackLeftKnight(25),
    BlackLeftBishop(26),
    BlackQueen(27),
    BlackKing(28),
    BlackRightBishop(29),
    BlackRightKnight(30),
    BlackRightCastle(31);

    private int value;

    PieceIndex(int value) {
        if ((value < -1) || (31 < value))
            throw new IllegalArgumentException(String.format("Invalid Piece Index: %d", value));

        this.value = value;
    }

    public int getValue() { return value; }
}
