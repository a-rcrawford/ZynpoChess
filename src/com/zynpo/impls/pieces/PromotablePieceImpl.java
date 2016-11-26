package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Pawn;
import com.zynpo.interfaces.pieces.PromotablePiece;


public abstract class PromotablePieceImpl extends ChessPieceImpl implements PromotablePiece {

    protected Pawn _origPawn;


    PromotablePieceImpl(PieceIndex index, ChessSquare square) {
        super(index, square);
        _origPawn = null;
    }


    PromotablePieceImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        super(index, square, sideColor);
        _origPawn = null;
    }


    PromotablePieceImpl(Pawn pawn) {
        super(pawn.getIndex(), pawn.getSquare(), pawn.getSideColor());
        _origPawn = pawn;

        if (null == _origPawn.getPromotedToPiece()) {
            _origPawn.setPromotedToPiece(this);
        }
    }


    @Override
    public Pawn getOrigPawn() { return _origPawn; }

}
