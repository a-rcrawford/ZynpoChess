package com.zynpo.impls;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.ChessPiece;

import java.util.Set;


class ChessSquareImpl implements ChessSquare, Comparable<ChessSquare> {

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

    @Override
    public int getIndex() { return (_row * _board.getColCount()) + _col; }

    @Override
    public int getRow(){ return _row; }

    @Override
    public int getCol(){ return _col; }

    @Override
    public SideColor getSideColor() {
        if (((_row + _col) & 1) == 1)
            return SideColor.White;
        else
            return SideColor.Black;
    }

    @Override
    public ChessBoard getBoard(){ return _board; }

    @Override
    public ChessPiece getPiece() { return _piece; }

    @Override
    public void setPiece(ChessPiece piece) {
        _piece = piece;
    }

    @Override
    public int rowsAwayFromCount(ChessSquare other) {
        if (other.getBoard() != _board) {
            throw new IllegalArgumentException("Can't calculate rows away from Square on different board");
        }

        return _row - other.getRow();
    }

    @Override
    public int colsAwayFromCount(ChessSquare other) {
        if (other.getBoard() != _board) {
            throw new IllegalArgumentException("Can't calculate columns away from Square on different board");
        }

        return _col - other.getCol();
    }

    @Override
    public int rowDistanceFrom(ChessSquare other) { return Math.abs(rowsAwayFromCount(other)); }

    @Override
    public int colDistanceFrom(ChessSquare other) {
        return Math.abs(colsAwayFromCount(other));
    }

    @Override
    public ChessSquare getRelativeSquare(int rowOffset, int colOffset) {
        int row = _row + rowOffset;
        int col = _col + colOffset;

        return _board.getSquare(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (!(obj instanceof ChessSquare)) {
            return false;
        }

        ChessSquare other = (ChessSquare) obj;

        if (this.getRow() != other.getRow()) {
            return false;
        }

        if (this.getCol() != other.getCol()) {
            return false;
        }

        if ((null == this.getPiece()) && (null == other.getPiece())) {
            return true;
        }

        if (null == this.getPiece()) {
            return false;
        }

        if (this.getPiece().equals(other.getPiece())) {
            return true;
        }

        return false;
    }


    @Override
    public int compareTo(ChessSquare other) {
        if (this.getRow() != other.getRow()) {
            return (this.getRow() < other.getRow()) ? -1 : 1;
        }

        if (this.getCol() != other.getCol()) {
            return (this.getCol() < other.getCol()) ? -1 : 1;
        }

        return 0;
    }


    @Override
    public boolean coveredBy(SideColor sideColor) {
        Set<ChessPiece> pieces = this.getBoard().getPiecesInPlay(
                SideColor.White == sideColor ? PieceFlags.AllWhitePieces : PieceFlags.AllBlackPieces);

        for (ChessPiece piece : pieces) {
            if (piece.covers(this)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ChessSquare clone(ChessBoard otherBoard) {
        ChessSquare square = new ChessSquareImpl(otherBoard, this.getRow(), this.getCol());

        return square;
    }


    @Override
    public String toString() {
        return String.format("%c%c", 'a' + _col, '1' + _row);
    }
}