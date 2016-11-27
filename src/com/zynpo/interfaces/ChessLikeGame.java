package com.zynpo.interfaces;

import com.zynpo.interfaces.pieces.ChessPiece;

public interface ChessLikeGame {

    /**
     * Anticipating Crazyhouse where pieces can drop
     * back into play after being taken.
     * @return true if pieces are permitted to drop back into play
     */
    boolean piecesCanDropBackIntoPlay();

    /**
     * Anticipating Crazyhouse where pieces switch
     * sides after being taken.
     * @return true if pieces switch sides after being taken
     */
    boolean takenPiecesSwitchSides();

    boolean isValidMove(ChessPiece pieceToMove, ChessSquare squareOccupied);


}
