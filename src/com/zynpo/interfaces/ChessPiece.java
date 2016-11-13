package com.zynpo.interfaces;

import com.zynpo.enums.SideColor;


public interface ChessPiece {

    ChessSquare getSquare();
    void setSquare(ChessSquare square);
    ChessSquare getInitialSquare();
    ChessBoard getBoard();

    SideColor getSideColor();
    SideColor getOpposingSideColor();
    boolean onSameSideAs(ChessPiece other);
    boolean opposesSideOf(ChessPiece other);

    int getMovedCount();
    void incrementMovedCount();
    boolean hasEverMoved();
    boolean neverMoved();

    boolean mightMoveTo(ChessSquare square);
    boolean covers(ChessSquare square);
    boolean coversAnyOf(Iterable<ChessSquare> squares);

    Iterable<ChessSquare> potentialMoveSquares();
    Iterable<ChessSquare> preemptivePotentialMoveSquares();
}