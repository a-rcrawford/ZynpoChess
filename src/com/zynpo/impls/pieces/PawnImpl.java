package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.Pawn;
import com.zynpo.interfaces.pieces.PromotablePiece;

import java.util.Set;


public class PawnImpl extends ChessPieceImpl implements Pawn {

    private PromotablePiece _promotedToPiece;

    PawnImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    PawnImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    @Override
    public String notation() { return "P"; }

    @Override
    public int materialValue() { return 1; }

    @Override
    public int advanceUnit() {
        switch (this.getSideColor()) {
            case White:
                return 1;
            case Black:
                return -1;
            default:
                throw new IllegalStateException("Pawn SideColor should have been determined");
        }
    }

    @Override
    public int gameStartRow() {
        switch (this.getSideColor()) {
            case White:
                return 1;
            case Black:
                return 6;
            default:
                throw new IllegalStateException("Pawn SideColor should have been determined");
        }
    }

    @Override
    public int jumpTwoRow() {
        switch (this.getSideColor()) {
            case White:
                return 3;
            case Black:
                return 4;
            default:
                throw new IllegalStateException("Pawn SideColor should have been determined");
        }
    }

    @Override
    public int promotionRow() {
        switch (this.getSideColor()) {
            case White:
                return 7;
            case Black:
                return 0;
            default:
                throw new IllegalStateException("Pawn SideColor should have been determined");
        }
    }

    @Override
    public ChessSquare squareJustInFront() {
        if (null != this.getSquare())
            return this.getSquare().getRelativeSquare(this.advanceUnit(), 0);
        else
            return null;
    }

    @Override
    public ChessSquare jumpTwoSquare() {
        // Pawns initially placed or dropped on the proper start row have the
        // option of jumping two squares initially ...
        if (this.getOrigSquare().getRow() == this.gameStartRow())
            return this.getOrigSquare().getRelativeSquare(2 * this.advanceUnit(), 0);
        else
            return null;
    }

    @Override
    public PromotablePiece getPromotedToPiece() {
        return _promotedToPiece;
    }

    @Override
    public void setPromotedToPiece(PromotablePiece piece) {
        _promotedToPiece = piece;

        if (this != piece.getOrigPawn()) {
            throw new IllegalStateException("this Pawn should be the original Pawn of the Promoted piece");
        }

        if (null != this.getSquare()) {
            this.setSquare(null);
        }
    }

    @Override
    public boolean covers(ChessSquare square) {
        if (this.getSquare().getRow() + this.advanceUnit() == square.getRow()) {
            if (this.getSquare().colDistanceFrom(square) == 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mightMoveTo(ChessSquare square) {
        if (!this.shallowMightMoveTo(square))
            return false;

        if (null == square.getPiece()) {
            // Pawns should always have a SquareJustInFront because they can't sit
            // on the PromotionRow without being promoted ...

            if (null == this.squareJustInFront().getPiece()) {

                if (this.squareJustInFront() == square) {
                    return true;
                }

                if (this.neverMoved() && (this.jumpTwoSquare() == square)) {
                    return true;
                }

            }
        }

        if (this.covers(square)) {
            if (null == square.getPiece()) {
                // TODO: Better check for possible en passant ...
                ChessSquare besideSquare = square.getRelativeSquare(-this.advanceUnit(), 0);
                ChessPiece besidePiece = besideSquare.getPiece();

                if ((null != besidePiece) && (besidePiece.opposesSideOf(this)) && (besidePiece instanceof Pawn)) {
                    return true;
                }

            } else {
                if (square.getPiece().opposesSideOf(this))
                    return true;
            }
        }

        return false;
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();
        ChessSquare potential;

        for(int colOffset = -1; colOffset <= 1; ++colOffset) {
            potential = this.getSquare().getRelativeSquare(this.advanceUnit(), colOffset);

            if (PotentialMoveReason.ForMoveAfterNext == reason) {
                potentials.add(potential); // Simply assume we might be able to move there
            } else if (this.mightMoveTo(potential)) {
                potentials.add(potential);
            }
        }

        potential = this.jumpTwoSquare();

        if ((null != potential) && (this.neverMoved())) {
            if (PotentialMoveReason.ForMoveAfterNext == reason) {
                potentials.add(potential); // Simply assume we might be able to move there
            } else if (this.mightMoveTo(potential)) {
                potentials.add(potential);
            }
        }

        return potentials;
    }

}