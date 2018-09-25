package com.zynpo.interfaces;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.pieces.ChessPiece;

import java.util.List;
import java.util.Set;


public interface ChessBoardState {

    ChessBoard getBoard();

    SideColor getSideToMove();
    SideColor getWinner();

    Set<ChessPiece> getPiecesInPlay(PieceFlags pieceFlags);
    Set<ChessPiece> getPiecesOutOfPlay(PieceFlags pieceFlags);

    List<MoveRecord> getValidMoves();
}
