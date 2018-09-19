package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Castle;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;

import java.util.Set;


public class KingImpl extends ChessPieceImpl implements King {

    KingImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    KingImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    @Override
    protected String name() {
        return "King";
    }

    @Override
    public String notation() { return "K"; }

    @Override
    public int materialValue() { return Integer.MAX_VALUE; }

    private ChessSquare castleLeftSquare() { return this.getSquare().getRelativeSquare(0, -2); }

    private ChessSquare castleRightSquare() { return this.getSquare().getRelativeSquare(0, 2); }

    private Castle leftCastle() {
        Castle leftCastle = null;

        if (0 == this.getMovedCount()) {
            ChessPiece leftPiece = this.getSquare().getRelativeSquare(0, -4).getPiece();

            if ((null != leftPiece) && (leftPiece instanceof Castle)) {
                leftCastle = (Castle) leftPiece;

                if (leftCastle.opposesSideOf(this) || (0 < leftCastle.getMovedCount())) {
                    leftCastle = null;
                }
            }
        }

        return leftCastle;
    }

    private Castle rightCastle() {
        Castle rightCastle = null;

        if (0 == this.getMovedCount()) {
            ChessPiece rightPiece = this.getSquare().getRelativeSquare(0, 3).getPiece();

            if ((null != rightPiece) && (rightPiece instanceof Castle)) {
                rightCastle = (Castle) rightPiece;

                if (rightCastle.opposesSideOf(this) || (0 < rightCastle.getMovedCount())) {
                    rightCastle = null;
                }
            }
        }

        return rightCastle;
    }

    @Override
    public boolean covers(ChessSquare square) {
        if (this.getSquare() == square) {
            return false;
        }

        if (!ChessSquare.canCompare(this.getOrigSquare(), square))
            return false;

        if (this.getSquare().colDistanceFrom(square) <= 1) {
            if (this.getSquare().rowDistanceFrom(square) <= 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mightMoveTo(ChessSquare square) {
        if (super.mightMoveTo(square)) {
            return true;
        }

        if ((0 == this.getMovedCount()) &&
            (2 == square.colDistanceFrom(this.getSquare())) &&
            ChessSquare.onSameRow(this.getSquare(), square)) {

            // It looks like the caller is asking whether a castle is valid ...
            Castle castle = null;
            ChessSquare castleDestSquare = null;

            {
                ChessPiece piece = null;

                if (this.getSquare().colsAwayFromCount(square) == -2) {
                    piece = this.getSquare().getRelativeSquare(0, -4).getPiece();
                    castleDestSquare = this.getSquare().getRelativeSquare(0, -1);
                } else if (this.castleRightSquare() == square) {
                    piece = this.getSquare().getRelativeSquare(0, 3).getPiece();
                    castleDestSquare = this.getSquare().getRelativeSquare(0, 1);
                }

                if ((null != piece) && (piece instanceof Castle)) {
                    castle = (Castle) piece;
                }
            }


            if ((null != castle) && (0 == castle.getMovedCount()) && castle.mightMoveTo(castleDestSquare)) {
                // Later make sure that the King must not
                // 1) be in check, 2) move through check, 3) move into check.
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        if (this.getMovedCount() == 0) {
            for (int colOffset : new int[] { 2, -2 }) {
                ChessSquare castleToSquare = this.getSquare().getRelativeSquare(0, colOffset);
                if (this.mightMoveTo(castleToSquare)) {
                    potentials.add(castleToSquare);
                }
            }
        }

        for(int rowDirection = -1; rowDirection <= 1; ++rowDirection ) {
            for (int colDirection = -1; colDirection <= 1; ++colDirection ) {
                if ((0 == rowDirection) && (0 == colDirection)) {
                    continue;
                }

                ChessSquare potential = this.getSquare().getRelativeSquare(rowDirection, colDirection);

                if (null == potential) {
                    break;
                }

                ChessPiece piece = potential.getPiece();

                if (null == piece) {
                    // Always assume this King can move to an empty Square ...
                    potentials.add(potential);
                } else {
                    if (PotentialMoveReason.ForNextMove == reason) {
                        if (piece.opposesSideOf(this)) {
                            // Assume this King can take an opposing Piece ...
                            potentials.add(potential);
                        }
                    } else if (PotentialMoveReason.ForMoveAfterNext == reason) {
                        potentials.add(potential);
                    }
                }
            }
        }

        return potentials;
    }

}
