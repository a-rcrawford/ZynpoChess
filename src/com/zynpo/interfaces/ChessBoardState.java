package com.zynpo.interfaces;

import com.zynpo.enums.GameStatus;
import com.zynpo.enums.SideColor;

import java.util.List;


public interface ChessBoardState {

    ChessBoard getBoard();

    SideColor getSideToMove();
    boolean sideToMoveIsInCheck();
    boolean sideToMoveHasValidMove();

    List<MoveRecord> getValidMoves();

    GameStatus getGameStatus();
}
