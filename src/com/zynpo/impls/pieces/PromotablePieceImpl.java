package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Pawn;
import com.zynpo.interfaces.pieces.PromotablePiece;


public class PromotablePieceImpl extends ChessPieceImpl implements PromotablePiece {

    protected Pawn _origPawn;


    PromotablePieceImpl(PieceIndex index, ChessSquare square) {
        super(index, square);
        _origPawn = null;
    }


    PromotablePieceImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        super(index, square, sideColor);
        _origPawn = null;
    }


    PromotablePieceImpl(PromotablePiece otherPiece, ChessSquare otherSquare) {
        super(otherPiece, otherSquare);
    }

    @Override
    public PromotablePiece clone(ChessSquare otherSquare) {
        return new PromotablePieceImpl(this, otherSquare);
    }


    public PromotablePieceImpl(Pawn pawn) {
        super(pawn.getIndex(), pawn.getSquare(), pawn.getSideColor());
        _origPawn = pawn;

        if (null == _origPawn.getPromotedToPiece()) {
            _origPawn.setPromotedToPiece(this);
        }
    }


    @Override
    protected String name() {
        return "Undetermined Promotion";
    }


    @Override
    public String notation() {
        // Meaning we don't know what this will be promoted to yet ...
        return "?";
    }


    @Override
    public int materialValue() {
        throw new InternalError("Can't ask for materialValue of Undetermined Promotion");
    }

    @Override
    public Pawn getOrigPawn() {
        return _origPawn;
    }

}
