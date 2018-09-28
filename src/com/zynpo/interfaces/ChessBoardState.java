package com.zynpo.interfaces;

import com.zynpo.enums.SideColor;

import java.util.List;


public interface ChessBoardState {

    ChessBoard getBoard();

    SideColor getSideToMove();
    boolean sideToMoveIsInCheck();

    List<MoveRecord> getValidMoves();
}
