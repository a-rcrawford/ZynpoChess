package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Castle;
import com.zynpo.interfaces.pieces.Pawn;


public class CastleImpl extends PromotablePieceImpl implements Castle {

    CastleImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    CastleImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    CastleImpl(Pawn pawn) { super(pawn); }

    @Override
    protected String name() {
        return "Castle";
    }

    @Override
    public String notation() { return "R"; } // ... for Rook

    @Override
    public int materialValue() { return 5; }

}
