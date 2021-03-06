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

    PawnImpl(PieceIndex index, ChessSquare square) {
        super(index, square);
    }

    PawnImpl(Pawn otherPawn, ChessSquare otherSquare) {
        super(otherPawn, otherSquare);
    }

    PawnImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        super(index, square, sideColor);
    }

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


    private boolean _takenByEnPassant = false;


    @Override
    public ChessPiece moveToSquare(ChessSquare square) {
        ChessSquare possibleEnPassantSquare = null;

        if ((this.getSquare() == this.getOrigSquare()) && (this.jumpTwoSquare() == square)) {

            for (int colOffset : new int[]{ -1, 1 }) {
                ChessSquare besideSquare = square.getRelativeSquare(0, colOffset);
                ChessPiece opposingPawn = null;

                if (null != besideSquare) {
                    opposingPawn = besideSquare.getPiece();

                    if ((null != opposingPawn) && (opposingPawn instanceof Pawn) && (opposingPawn.opposesSideOf(this))) {
                        possibleEnPassantSquare = this.squareJustInFront();
                        break;
                    }
                }
            }
        }

        ChessPiece takenPiece = super.moveToSquare(square);

        if (this.getBoard().getEnPassantSquare() == square) {
            if (null != takenPiece) {
                throw new InternalError("En-passant square shouldn't have been occupied by a " + takenPiece);
            }

            takenPiece = (Pawn) square.getRelativeSquare( -this.advanceUnit(), 0).getPiece();
            ((PawnImpl) takenPiece).setSquare(null);
            ((PawnImpl) takenPiece)._takenByEnPassant = true;
        }

        ((ChessBoardImpl) this.getBoard()).setEnPassantSquare(possibleEnPassantSquare);

        return takenPiece;
    }


    @Override
    public void takeBackToSquare(ChessSquare square, ChessPiece formerlyTakenPiece) {
        super.takeBackToSquare(square, formerlyTakenPiece);

        if ((formerlyTakenPiece instanceof Pawn) && (1 == formerlyTakenPiece.getMovedCount())) {
            // That last move could have been en-passant ...
            PawnImpl formerlyTakenPawn = (PawnImpl) formerlyTakenPiece;
            if (formerlyTakenPawn._takenByEnPassant) {
                formerlyTakenPawn.setSquare(formerlyTakenPawn.squareJustInFront());
                formerlyTakenPawn._takenByEnPassant = false;
            }
        }
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

        if ((null != potential) && (this.getSquare().getRow() == this.gameStartRow()) && (0 == this.getMovedCount())) {
            if (PotentialMoveReason.ForMoveAfterNext == reason) {
                potentials.add(potential); // Simply assume we might be able to move there
            } else if (this.mightMoveTo(potential)) {
                potentials.add(potential);
            }
        }

        return potentials;
    }


    @Override
    public Pawn clone(ChessSquare otherSquare) {
        PawnImpl clonedPawn = new PawnImpl(this, otherSquare);
        clonedPawn._takenByEnPassant = _takenByEnPassant;
        return clonedPawn;
    }

}
