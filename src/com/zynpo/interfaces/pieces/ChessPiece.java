package com.zynpo.interfaces.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;

import java.util.Set;


public interface ChessPiece {

    ChessSquare getSquare();

    /**
     * Move this piece to the given parameter
     * @param square to be occupied by this Piece
     */
    void dropToSquare(ChessSquare square);
    ChessPiece moveToSquare(ChessSquare square);
    void takeBackToSquare(ChessSquare square, ChessPiece formerlyTakenPiece);

    ChessSquare getOrigSquare();
    ChessBoard getBoard();
    String notation();

    SideColor getSideColor();
    boolean onSameSideAs(ChessPiece other);
    boolean opposesSideOf(ChessPiece other);

    int getMovedCount();

    PieceIndex getIndex();
    int materialValue();

    boolean mightMoveTo(ChessSquare square);
    boolean covers(ChessSquare square);
    boolean coversAnyOf(Iterable<ChessSquare> squares);

    Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason);

    ChessPiece clone(ChessSquare otherSquare);
}
