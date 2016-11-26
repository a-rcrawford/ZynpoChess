package com.zynpo.interfaces;


import com.zynpo.enums.PieceIndex;
import com.zynpo.interfaces.pieces.ChessPiece;

public interface ChessBoard extends Iterable<ChessSquare> {

    static final int ROW_COUNT = 8;
    static final int COL_COUNT = 8;

    int getRowCount();
    int getColCount();

    boolean squareExists(int row, int col);

    ChessSquare getSquare(int row, int col);
    ChessSquare getSquare(String notation);
    ChessSquare getSquare(int index);

    ChessPiece getPiece(int index);
    ChessPiece getPiece(PieceIndex index);
}
