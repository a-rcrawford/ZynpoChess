package com.zynpo.interfaces.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;

import java.util.Set;


public interface ChessPiece {

    static final int PIECES_PER_BOARD_COUNT = 32;

    ChessSquare getSquare();

    /**
     * Set this Piece's square to the given parameter
     * @param square to be occupied by this Piece
     * @return the square that was occupied by this piece prior
     */
    ChessSquare setSquare(ChessSquare square);
    ChessSquare getOrigSquare();
    ChessBoard getBoard();
    String notation();

    SideColor getSideColor();
    SideColor getOpposingSideColor();
    boolean onSameSideAs(ChessPiece other);
    boolean opposesSideOf(ChessPiece other);

    int getMovedCount();

    /**
     * Increment the count of times this piece was moved
     * @return the end resulting number of times this piece was moved
     */
    int incrementMovedCount();
    boolean hasEverMoved();
    boolean neverMoved();

    PieceIndex getIndex();
    int materialValue();

    boolean mightMoveTo(ChessSquare square);
    boolean covers(ChessSquare square);
    boolean coversAnyOf(Iterable<ChessSquare> squares);

    Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason);
}