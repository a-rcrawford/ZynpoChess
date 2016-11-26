package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Knight;
import com.zynpo.interfaces.pieces.Pawn;

import java.util.Set;

public class KnightImpl extends PromotablePieceImpl implements Knight {

    KnightImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    KnightImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        super(index, square, sideColor);
    }

    KnightImpl(Pawn pawn) { super(pawn); }

    @Override
    public String notation() { return "N"; } // can't use "K" because that's for King

    @Override
    public int materialValue() { return 3; }

    @Override
    public boolean covers(ChessSquare square) {
        return this.coversLikeKnight(square);
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowOffset : new int[]{ -2, -1, 1, 2 } ) {
            for (int colOffset : new int[]{ -2, -1, 1, 2 } ) {
                if (Math.abs(rowOffset) != Math.abs(colOffset)) {
                    ChessSquare potential = this.getSquare().getRelativeSquare(rowOffset, colOffset);

                    if (null != potential) {
                        ChessPiece piece = potential.getPiece();

                        if (null == piece) {
                            // Always assume this Knight can move to an empty Square ...
                            potentials.add(potential);
                        } else {
                            if (PotentialMoveReason.ForNextMove == reason) {
                                if (piece.opposesSideOf(this)) {
                                    // Assume this Knight can take an opposing Piece ...
                                    potentials.add(potential);
                                }
                            } else if (PotentialMoveReason.ForMoveAfterNext == reason) {
                                // Only assume this Knight won't land on its own King
                                // for the move after next ...
                                if (!((piece instanceof King) && piece.onSameSideAs(this))) {
                                    potentials.add(potential);
                                }
                            }
                        }
                    }
                }
            }
        }

        return potentials;
    }
}
