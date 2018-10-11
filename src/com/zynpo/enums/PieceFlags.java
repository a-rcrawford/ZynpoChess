package com.zynpo.enums;


import com.zynpo.interfaces.pieces.*;

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
    AllPawns(AllWhitePawns._value | AllBlackPawns._value),

    WhiteCastles(WhiteLeftCastle._value | WhiteRightCastle._value),
    BlackCastles(BlackLeftCastle._value | BlackRightCastle._value),
    AllCastles(WhiteCastles._value | BlackCastles._value),

    WhiteKnights(WhiteLeftKnight._value | WhiteRightKnight._value),
    BlackKnights(BlackLeftKnight._value | BlackRightKnight._value),
    AllKnights(WhiteKnights._value | BlackKnights._value),

    WhiteBishops(WhiteLeftBishop._value | WhiteRightBishop._value),
    BlackBishops(BlackLeftBishop._value | BlackRightBishop._value),
    AllBishops(WhiteBishops._value | BlackBishops._value),

    AllQueens(WhiteQueen._value | BlackQueen._value),

    BothKings(WhiteKing._value | BlackKing._value),

    PromotablePieces(AllCastles._value | AllKnights._value | AllBishops._value | AllQueens._value),

    AllPieces(0xFFFFFFFF),

    AllPiecesOtherThanKings(AllPieces._value & ~BothKings._value);

    private int _value;

    public int getValue() {
        return _value;
    }

    static public int fromIndex(PieceIndex index) {
        return 1 << index.getValue();
    }

    PieceFlags(int value) {
        _value = value;
    }

    PieceFlags(PieceIndex index) {
        _value = fromIndex(index);
    }

    public static int piecesOfSameSide(PieceFlags pieceFlags, SideColor sideColor) {
        return piecesOfSameSide(pieceFlags.getValue(), sideColor);
    }

    public static int piecesOfSameSide(int pieceFlags, SideColor sideColor) {
        if (SideColor.White == sideColor) {
            return PieceFlags.AllWhitePieces.getValue() & pieceFlags;
        } else {
            return PieceFlags.AllBlackPieces.getValue() & pieceFlags;
        }
    }

    public static int similarPiecesOfSameSide(ChessPiece piece) {
        int similarPieceFlags;

        if (piece instanceof Pawn) {
            similarPieceFlags = PieceFlags.AllPawns.getValue();
        } else if (piece instanceof Castle) {
            similarPieceFlags = PieceFlags.AllCastles.getValue();
        } else if (piece instanceof Knight) {
            similarPieceFlags = PieceFlags.AllKnights.getValue();
        } else if (piece instanceof Bishop) {
            similarPieceFlags = PieceFlags.AllBishops.getValue();
        } else if (piece instanceof Queen) {
            similarPieceFlags = PieceFlags.AllQueens.getValue();
        } else {
            throw new InternalError("Shouldn't be asking for similarPiecesOfSameSide() on " + piece);
        }

        // Don't return flags that match the piece paramater passed in ...
        similarPieceFlags &= ~(1 << piece.getIndex().getValue());

        if (SideColor.White == piece.getSideColor()) {
            return PieceFlags.AllWhitePieces.getValue() & similarPieceFlags;
        } else {
            return PieceFlags.AllBlackPieces.getValue() & similarPieceFlags;
        }
    }

    public boolean containsAllOf(int pieceFlags) {
        return this.getValue() == (this.getValue() | pieceFlags);
    }

    public boolean containsAnyOf(int pieceFlags) {
        return 0 != (this.getValue() & pieceFlags);
    }

    public boolean containsNoneOf(int pieceFlags) {
        return 0 == (this.getValue() & pieceFlags);
    }

    public boolean contains(PieceIndex index) {
        return this.containsAllOf(1 << index.getValue());
    }
}