package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessBoardImpl;
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
    protected String name() {
        return "Pawn";
    }

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
        if (null == this.getOrigSquare()) {
            return null;
        }

        if (this.getOrigSquare().getRow() == this.gameStartRow()) {
            return this.getOrigSquare().getRelativeSquare(2 * this.advanceUnit(), 0);
        } else {
            return null;
        }
    }

    @Override
    public ChessSquare setSquare(ChessSquare square) {
        ChessSquare possibleEnPassantSquare = null;

        if ((this.getSquare() == this.getOrigSquare())
            && (this.jumpTwoSquare() == square)) {
            possibleEnPassantSquare = this.squareJustInFront();
        }

        ChessSquare priorSquare = super.setSquare(square);
        ((ChessBoardImpl) this.getBoard()).setEnPassantSquare(possibleEnPassantSquare);

        return priorSquare;
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
        if (this.getSquare() == square) {
            return false;
        }

        if (!ChessSquare.canCompare(this.getSquare(), square)) {
            return false;
        }

        ChessPiece otherPiece = square.getPiece();

        if ((null != otherPiece) && (this.onSameSideAs(otherPiece))) {
            return false; // Pieces never capture pieces from their own side
        }

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

                if (this.getBoard().getEnPassantSquare() == square) {
                    return true;
                }

            } else {
                if (square.getPiece().opposesSideOf(this)) {
                    return true;
                }
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

        if ((null != potential) && (this.getSquare().getRow() == this.gameStartRow()) && (this.neverMoved())) {
            if (PotentialMoveReason.ForMoveAfterNext == reason) {
                potentials.add(potential); // Simply assume we might be able to move there
            } else if (this.mightMoveTo(potential)) {
                potentials.add(potential);
            }
        }

        return potentials;
    }

}
