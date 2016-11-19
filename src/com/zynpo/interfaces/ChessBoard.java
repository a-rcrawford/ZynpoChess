package com.zynpo.interfaces;


public interface ChessBoard extends Iterable<ChessSquare> {

    int getRowCount();
    int getColCount();

    boolean squareExists(int row, int col);
    ChessSquare getSquare(int row, int col);
    ChessSquare getSquare(String notation);
    ChessSquare getSquare(int index);
}
