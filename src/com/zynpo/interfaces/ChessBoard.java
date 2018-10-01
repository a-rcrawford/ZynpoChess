package com.zynpo.interfaces;


import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.PieceIndex;
import com.zynpo.interfaces.pieces.ChessPiece;

import java.util.Set;

public interface ChessBoard extends Iterable<ChessSquare> {

    int getRowCount();
    int getColCount();

    boolean squareExists(int row, int col);

    ChessSquare getSquare(int index);
    ChessSquare getSquare(int row, int col);
    ChessSquare getSquare(String notation);
    Set<ChessSquare> getSquares(String... notations);

    ChessPiece getPiece(PieceIndex index);
    ChessPiece getPiece(int index);

    Set<ChessPiece> getPiecesInPlay(PieceFlags pieceFlags);
    Set<ChessPiece> getPiecesOutOfPlay(PieceFlags pieceFlags);

    ChessSquare getEnPassantSquare();

    ChessBoard clone();
}
