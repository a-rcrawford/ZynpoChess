package com.zynpo.enums;


public enum PieceFlags {
    None(0),
    WhiteLeftCastle(PieceIndex.WhiteLeftCastle),
    WhiteLeftKnight(PieceIndex.WhiteLeftKnight),
    WhiteLeftBishop(PieceIndex.WhiteLeftBishop),
    WhiteQueen(PieceIndex.WhiteQueen),
    WhiteKing(PieceIndex.WhiteKing),
    WhiteRightBishop(PieceIndex.WhiteRightBishop),
    WhiteRightKnight(PieceIndex.WhiteRightKnight),
    WhiteRightCastle(PieceIndex.WhiteRightCastle),

    WhitePawnA(PieceIndex.WhitePawnA),
    WhitePawnB(PieceIndex.WhitePawnB),
    WhitePawnC(PieceIndex.WhitePawnC),
    WhitePawnD(PieceIndex.WhitePawnD),
    WhitePawnE(PieceIndex.WhitePawnE),
    WhitePawnF(PieceIndex.WhitePawnF),
    WhitePawnG(PieceIndex.WhitePawnG),
    WhitePawnH(PieceIndex.WhitePawnH),

    BlackPawnA(PieceIndex.BlackPawnA),
    BlackPawnB(PieceIndex.BlackPawnB),
    BlackPawnC(PieceIndex.BlackPawnC),
    BlackPawnD(PieceIndex.BlackPawnD),
    BlackPawnE(PieceIndex.BlackPawnE),
    BlackPawnF(PieceIndex.BlackPawnF),
    BlackPawnG(PieceIndex.BlackPawnG),
    BlackPawnH(PieceIndex.BlackPawnH),

    BlackLeftCastle(PieceIndex.BlackLeftCastle),
    BlackLeftKnight(PieceIndex.BlackLeftKnight),
    BlackLeftBishop(PieceIndex.BlackLeftBishop),
    BlackQueen(PieceIndex.BlackQueen),
    BlackKing(PieceIndex.BlackKing),
    BlackRightBishop(PieceIndex.BlackRightBishop),
    BlackRightKnight(PieceIndex.BlackRightKnight),
    BlackRightCastle(PieceIndex.BlackRightCastle),

    AllWhitePieces(0x0000FFFF),
    AllBlackPieces(0xFFFF0000),

    AllWhitePawns(0x0000FF00),
    AllBlackPawns(0x00FF0000),
    AllPawns(AllWhitePawns.value | AllBlackPawns.value),

    WhiteCastles(WhiteLeftCastle.value | WhiteRightCastle.value),
    BlackCastles(BlackLeftCastle.value | BlackRightCastle.value),
    AllCastles(WhiteCastles.value | BlackCastles.value),

    WhiteKnights(WhiteLeftKnight.value | WhiteRightKnight.value),
    BlackKnights(BlackLeftKnight.value | BlackRightKnight.value),
    AllKnights(WhiteKnights.value | BlackKnights.value),

    WhiteBishops(WhiteLeftBishop.value | WhiteRightBishop.value),
    BlackBishops(BlackLeftBishop.value | BlackRightBishop.value),
    AllBishops(WhiteBishops.value | BlackBishops.value),

    BothQueens(WhiteQueen.value | BlackQueen.value),

    BothKings(WhiteKing.value | BlackKing.value),

    AllPieces(0xFFFFFFFF);

    private int value;
    public int getValue() { return value; }
    static public int fromIndex(PieceIndex index) { return 1 << index.getValue(); }

    PieceFlags(int value) { this.value = value; }
    PieceFlags(PieceIndex index) { value = fromIndex(index); }


    public boolean containsAllOf(int pieceFlags) {
        return (this.getValue() == (this.getValue() | pieceFlags));
    }

    public boolean containsNoneOf(int pieceFlags) {
        return (0 == (this.getValue() & pieceFlags));
    }

    public boolean contains(PieceIndex index) {
        return this.containsAllOf(fromIndex(index));
    }
}