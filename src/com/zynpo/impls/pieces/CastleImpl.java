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


public class CastleImpl extends PromotablePieceImpl implements Castle {

    CastleImpl(PieceIndex index, ChessSquare square) { super(index, square); }

    CastleImpl(PieceIndex index, ChessSquare square, SideColor sideColor) { super(index, square, sideColor); }

    CastleImpl(Pawn pawn) { super(pawn); }

    @Override
    public String notation() { return "R"; } // ... for Rook

    @Override
    public int materialValue() { return 5; }

    @Override
    public boolean covers(ChessSquare square) {
        return this.coversLikeCastle(square);
    }

    @Override
    public ChessSquare castleWithKingDestinationSquare() {
        int leftmostCol = 0;
        int rightmostCol = this.getBoard().getColCount() - 1;

        if (this.getOrigSquare().getCol() == leftmostCol)
            return this.getOrigSquare().getRelativeSquare(0, 3);
        else if (this.getOrigSquare().getCol() == rightmostCol)
            return this.getOrigSquare().getRelativeSquare(0, -2);
        else
            return null;
    }

    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowDirection = -1; rowDirection <= 1; ++rowDirection ) {
            for (int colDirection = -1; colDirection <= 1; ++colDirection) {
                if (Math.abs(rowDirection) == Math.abs(colDirection))
                    continue;

                for (int step = 1; ; ++step) {
                    ChessSquare potential = this.getSquare().getRelativeSquare(rowDirection * step, colDirection * step);

                    if (null == potential) {
                        // We already ran off the end of the board ...
                        break;
                    }

                    ChessPiece piece = potential.getPiece();

                    if (null == piece) {
                        // Always assume this Castle can move to an empty Square ...
                        potentials.add(potential);
                    } else {
                        if (PotentialMoveReason.ForNextMove == reason) {
                            if (piece.opposesSideOf(this)) {
                                // Assume this Castle can take an opposing Piece ...
                                potentials.add(potential);
                            }

                            // Step no further in this direction, because this
                            // Castle is blocked from here on ...
                            break;
                        } else if (PotentialMoveReason.ForMoveAfterNext == reason) {
                            // Only assume this Castle won't land on, or move through, its own King
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
