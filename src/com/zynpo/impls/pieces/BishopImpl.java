package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Bishop;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Pawn;

import java.util.Set;


public class BishopImpl extends PromotablePieceImpl implements Bishop {

    BishopImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    BishopImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        super(index, square, sideColor);
    }

    BishopImpl(Pawn pawn) { super(pawn); }

    @Override
    protected String name() {
        return "Bishop";
    }

    @Override
    public String notation() { return "B"; }

    @Override
    public int materialValue() { return 3; }

    @Override
    public boolean covers(ChessSquare square) {
        return this.coversLikeBishop(square);
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowDirection : new int[]{ -1, 1 } ) {
            for (int colDirection : new int[]{ -1, 1 } ) {
                for (int step = 1; ; ++step) {
                    ChessSquare potential = this.getSquare().getRelativeSquare(rowDirection * step, colDirection * step);

                    if (null != potential) {
                        ChessPiece piece = potential.getPiece();

                        if (null == piece) {
                            // Always assume this Bishop can move to an empty Square ...
                            potentials.add(potential);
                        } else {
                            if (PotentialMoveReason.ForNextMove == reason) {
                                if (piece.opposesSideOf(this)) {
                                    // Assume this Bishop can take an opposing Piece ...
                                    potentials.add(potential);
                                }

                                // Step no further in this direction, because this
                                // Bishop is blocked from here on ...
                                break;
                            } else if (PotentialMoveReason.ForMoveAfterNext == reason) {
                                // Only assume this Bishop won't land on, or move through, its own King
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
