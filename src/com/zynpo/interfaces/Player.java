package com.zynpo.interfaces;

import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;

import java.lang.reflect.Type;
import java.util.Set;

public interface Player {
    SideColor sideColor();
    Player opposingPlayer();
    ChessLikeGame getGame();
    ChessBoard getBoard();

    King getKing();

    Set<ChessPiece> getPiecesInPlay();
    Set<ChessPiece> getPiecesOutOfPlay();

    Set<ChessPiece> getPiecesInPlay(Type pieceType);
    Set<ChessPiece> getPiecesOutOfPlay(Type pieceType);

    void putPieceInPlay(ChessPiece piece);
    void takePieceOutOfPlay(ChessPiece piece);

    boolean covers(ChessSquare square);
    boolean coversAnyOf(Set<ChessSquare> squares);

    Set<ChessPiece> piecesThatMightMoveToSquare(Type pieceType, ChessSquare toSquare);
    ChessPiece pieceThatMightMoveToSquare(Type pieceType, ChessSquare toSquare);
}
