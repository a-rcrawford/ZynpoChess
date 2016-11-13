package com.zynpo.impls;

import com.zynpo.interfaces.*;

class ChessBoardImpl implements ChessBoard {

    private ChessSquare[] _squares;

    public int getRowCount() { return 8; }
    public int getColCount() { return 8; }
    public int getSquareCount() { return getRowCount() * getColCount(); }

    ChessBoardImpl() {
        _squares = new ChessSquare[getRowCount() * getColCount()];

        for (int row = 0; row < getRowCount(); ++row) {
            for (int col = 0; col < getColCount(); ++col) {
                int i = row * getColCount() + col;
                _squares[i] = ChessFactory.createSquare(this, row, col);
            }
        }
    }


    public boolean squareExists(int row, int col) {
        return (0 <= row) &&
                (row < getRowCount()) &&
                (0 <= col) &&
                (col < getColCount());
    }


    public ChessSquare getSquare(int row, int col) {
        if (squareExists(row, col))
            return _squares[row * getColCount() + col];
        else
            return null;
    }


    public ChessSquare getSquare(String notation) {
        notation = notation.trim().toLowerCase();

        if (notation.length() != 2)
            throw new IllegalArgumentException("Unexpected square notation: " + notation);

        char[] chars = notation.toCharArray();

        int col = (int) (chars[0] - 'a');
        int row = (int) (chars[1] - '1');

        return getSquare(row, col);
    }

}
