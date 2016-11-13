package com.zynpo.impls;

import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.*;


class ChessSquareImpl implements ChessSquare {

    private ChessBoard _board;
    private int _row;
    private int _col;
    private ChessPiece _piece;


    ChessSquareImpl(ChessBoard board, int row, int col) {
        _board = board;
        _row = row;
        _col = col;
        _piece = null;
    }


    public int getRow(){ return _row; }
    public int getCol(){ return _col; }


    public SideColor getSideColor() {
        if (((_row + _col) & 1) == 1)
            return SideColor.White;
        else
            return SideColor.Black;
    }

    public ChessBoard getBoard(){ return _board; }
    public boolean isOccupied() { return (null != _piece); }
    public boolean isUnoccupied() { return (null == _piece); }
    public ChessPiece getPiece() { return _piece; }

    public void setPiece(ChessPiece piece) { _piece = piece; }


    public int rowsAwayFromCount(ChessSquare other) {
        if (other.getBoard() != _board) {
            throw new IllegalArgumentException("Can't calculate rows away from Square on different board");
        }

        return other.getRow() - _row;
    }


    public int colsAwayFromCount(ChessSquare other) {
        if (other.getBoard() != _board) {
            throw new IllegalArgumentException("Can't calculate columns away from Square on different board");
        }

        return other.getCol() - _col;
    }


    public int rowDistanceFrom(ChessSquare other) {
        return Math.abs(rowsAwayFromCount(other));
    }
    public int colDistanceFrom(ChessSquare other) {
        return Math.abs(colsAwayFromCount(other));
    }


    public ChessSquare getRelativeSquare(int rowOffset, int colOffset) {
        int row = _row + rowOffset;
        int col = _col + colOffset;

        return _board.getSquare(row, col);
    }


    public String toString() {
        return String.format("%c%c", 'a' + _col, '1' + _row);
    }
}