package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Pawn;
import com.zynpo.interfaces.pieces.Queen;


public class QueenImpl extends PromotablePieceImpl implements Queen {

    QueenImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    QueenImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    QueenImpl(Queen otherQueen, ChessSquare otherSquare) {
        super(otherQueen, otherSquare);
    }

    public QueenImpl(Pawn pawn) { super(pawn); }

    @Override
    protected String name() {
        return "Queen";
    }

    @Override
    public String notation() { return "Q"; }

    @Override
    public int materialValue() { return 9; }

    @Override
    public Queen clone(ChessSquare otherSquare) {
        return new QueenImpl(this, otherSquare);
    }

}
