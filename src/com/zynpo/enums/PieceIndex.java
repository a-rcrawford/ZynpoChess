package com.zynpo.enums;


import com.zynpo.interfaces.ChessBoard;

public enum PieceIndex {
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

    BlackPawnA(16),
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

    private int _value;
    private static PieceIndex[] _allValues = values();

    PieceIndex(int value) {
        if ((value < -1) || (31 < value))
            throw new IllegalArgumentException(String.format("Invalid Piece Index: %d", value));

        this._value = value;
    }

    public int getValue() { return _value; }

    public static PieceIndex fromOrdinal(int i) { return _allValues[i]; }

    public int origRow() {
        int rowIndex = _value / ChessBoard.COL_COUNT;

        if (2 <= rowIndex)
            rowIndex += 4;

        return rowIndex;
    }

    public int origCol() {
        return _value % ChessBoard.COL_COUNT;
    }

    public SideColor origSideColor() {
        if (_value <= WhitePawnH.getValue())
            return SideColor.White;
        else
            return SideColor.Black;
    }
}
