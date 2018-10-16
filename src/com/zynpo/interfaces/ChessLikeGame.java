package com.zynpo.interfaces;

import com.zynpo.enums.GameStatus;
import com.zynpo.exceptions.InvalidMoveException;
import com.zynpo.exceptions.MoveException;
import com.zynpo.interfaces.pieces.ChessPiece;

import java.util.Set;

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

    GameStatus doMove(String notation) throws MoveException;

    void takeBackLastMove();

    ChessBoardState reviewFirst();
    ChessBoardState reviewNext() throws IndexOutOfBoundsException;
    ChessBoardState reviewPrevious() throws IndexOutOfBoundsException;
    ChessBoardState reviewLast();

    String getAllMoves();
    String loadAllMoves(String csvMoves) throws MoveException;

    boolean playerToMoveCanForceDraw();

    boolean setOverallGameStatus(GameStatus gameStatus);
    GameStatus getOverallGameStatus();
}
