package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Pawn;
import com.zynpo.interfaces.pieces.Queen;

import java.util.Set;


public class QueenImpl extends PromotablePieceImpl implements Queen {

    QueenImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    QueenImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    QueenImpl(Pawn pawn) { super(pawn); }

    @Override
    protected String name() {
        return "Queen";
    }

    @Override
    public String notation() { return "Q"; }

    @Override
    public int materialValue() { return 9; }

    @Override
    public boolean covers(ChessSquare square) {
        return this.coversLikeCastle(square) || this.coversLikeBishop(square);
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowDirection = -1; rowDirection <= 1; ++rowDirection ) {
            for (int colDirection = -1; colDirection <= 1; ++colDirection ) {
                if ((0 == rowDirection) && (0 == colDirection))
                    continue;

                for (int step = 1; ; ++step) {
                    ChessSquare potential = this.getSquare().getRelativeSquare(rowDirection * step, colDirection * step);

                    if (null == potential) {
                        break;
                    }

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

        return potentials;
    }

}
