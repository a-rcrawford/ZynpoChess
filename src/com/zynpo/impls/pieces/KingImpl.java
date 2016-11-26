package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Castle;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Pawn;

import java.util.Set;


public class KingImpl extends PromotablePieceImpl implements King {

    KingImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    KingImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    KingImpl(Pawn pawn) { super(pawn); }

    @Override
    public String notation() { return "K"; }

    @Override
    public int materialValue() { return Integer.MAX_VALUE; }

    @Override
    public ChessSquare castleLeftSquare() { return this.getSquare().getRelativeSquare(0, -2); }

    @Override
    public ChessSquare castleRightSquare() { return this.getSquare().getRelativeSquare(0, 2); }

    @Override
    public Castle leftCastle() {
        Castle leftCastle = null;

        if (this.neverMoved()) {
            ChessPiece leftPiece = this.getSquare().getRelativeSquare(0, -4).getPiece();

            if ((null != leftPiece) && (leftPiece instanceof Castle)) {
                leftCastle = (Castle) leftPiece;

                if (leftCastle.opposesSideOf(this) || leftCastle.hasEverMoved()) {
                    leftCastle = null;
                }
            }
        }

        return leftCastle;
    }

    @Override
    public Castle rightCastle() {
        Castle rightCastle = null;

        if (this.neverMoved()) {
            ChessPiece rightPiece = this.getSquare().getRelativeSquare(0, 3).getPiece();

            if ((null != rightPiece) && (rightPiece instanceof Castle)) {
                rightCastle = (Castle) rightPiece;

                if (rightCastle.opposesSideOf(this) || rightCastle.hasEverMoved()) {
                    rightCastle = null;
                }
            }
        }

        return rightCastle;
    }

    @Override
    public boolean covers(ChessSquare square) {
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
        if (super.mightMoveTo(square))
            return true;

        if (this.neverMoved() &&
            (2 == square.colDistanceFrom(this.getSquare())) &&
            ChessSquare.onSameRow(this.getSquare(), square)) {

            // It looks like the caller is asking whether a castle is valid ...
            Castle castle = null;

            if (this.castleLeftSquare() == square) {
                castle = this.leftCastle();
            } else if (this.castleRightSquare() == square) {
                castle = this.rightCastle();
            }

            if (null != castle) {
                if (castle.mightMoveTo(castle.castleWithKingDestinationSquare())) {
                    // TODO: If this King is not in Check
                    // TODO: castleWithKingDestinationSquare is not covered by an opposing piece
                    // TODO: The King isn't castling into Check
                    // TODO: Then the king may castle ...
                    // Just assume we can for now ...
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowDirection = -1; rowDirection <= 1; ++rowDirection ) {
            for (int colDirection = -1; colDirection <= -1; ++colDirection ) {
                if ((0 == rowDirection) && (0 == colDirection))
                    continue;

                for (int step = 1; ; ++step) {
                    ChessSquare potential = this.getSquare().getRelativeSquare(rowDirection * step, colDirection * step);

                    if (null != potential) {
                        ChessPiece piece = potential.getPiece();

                        if (null == piece) {
                            // Always assume this Queen can move to an empty Square ...
                            potentials.add(potential);
                        } else {
                            if (PotentialMoveReason.ForNextMove == reason) {
                                if (piece.opposesSideOf(this)) {
                                    // Assume this Queen can take an opposing Piece ...
                                    potentials.add(potential);
                                }

                                // Step no further in this direction, because this
                                // Queen is blocked from here on ...
                                break;
                            } else if (PotentialMoveReason.ForMoveAfterNext == reason) {
                                // Only assume this Queen won't land on, or move through, its own King
                                // for the move after next ...
                                if ((piece instanceof King) && piece.onSameSideAs(this)) {
                                    break;
                                }

                                potentials.add(potential);
                            }
                        }
                    }
                }
            }
        }

        return potentials;
    }

}
