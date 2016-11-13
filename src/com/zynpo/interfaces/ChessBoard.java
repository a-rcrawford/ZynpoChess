package com.zynpo.interfaces;


public interface ChessBoard {

    int getRowCount();
    int getColCount();
    int getSquareCount();

    boolean squareExists(int row, int col);
    ChessSquare getSquare(int row, int col);
    ChessSquare getSquare(String notation);
}