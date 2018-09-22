package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Bishop;
import com.zynpo.interfaces.pieces.Pawn;


public class BishopImpl extends PromotablePieceImpl implements Bishop {

    BishopImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    BishopImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        super(index, square, sideColor);
    }

    BishopImpl(Pawn pawn) { super(pawn); }

    @Override
    protected String name() {
        return "Bishop";
    }

    @Override
    public String notation() { return "B"; }

    @Override
    public int materialValue() { return 3; }

}
