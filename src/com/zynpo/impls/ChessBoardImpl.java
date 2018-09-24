package com.zynpo.impls;

import com.zynpo.constant.ChessBoardSpecs;
import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.pieces.ChessPieceFactory;
import com.zynpo.interfaces.*;
import com.zynpo.interfaces.pieces.ChessPiece;

import java.util.Iterator;
import java.util.Set;


public class ChessBoardImpl implements ChessBoard {

    private ChessSquare[] _squares;
    private ChessPiece[] _pieces;

    private ChessSquare _enPassantSquare = null;

    @Override
    public int getRowCount() { return ChessBoardSpecs.ROW_COUNT; }

    @Override
    public int getColCount() { return ChessBoardSpecs.COL_COUNT; }


    ChessBoardImpl() {
        _squares = new ChessSquare[getRowCount() * getColCount()];

        for (int row = 0; row < getRowCount(); ++row) {
            for (int col = 0; col < getColCount(); ++col) {
                int i = row * getColCount() + col;
                _squares[i] = ChessFactory.createSquare(this, row, col);
            }
        }

        _pieces = new ChessPiece[PieceIndex.values().length];

        for (int i = 0; i < _pieces.length; ++i) {
            _pieces[i] = ChessPieceFactory.createPiece(i, this);
        }
    }


    private
    ChessBoardImpl(ChessBoard otherBoard) {
        if (this.getRowCount() != otherBoard.getRowCount()) {
            throw new InternalError("Cloning a board with an unexpected number of rows");
        }

        if (this.getColCount() != otherBoard.getColCount()) {
            throw new InternalError("Cloning a board with an unexpected number of columns");
        }

        _squares = new ChessSquare[getRowCount() * getColCount()];

        for (int row = 0; row < getRowCount(); ++row) {
            for(int col = 0; col < getColCount(); ++col) {
                int i = row * getColCount() + col;
                _squares[i] = otherBoard.getSquare(row, col).clone(this);
            }
        }

        this.setEnPassantSquare(otherBoard.getEnPassantSquare());
    }


    @Override
    public ChessBoard clone() {
        return new ChessBoardImpl(this);
    }


    @Override
    public boolean squareExists(int row, int col) {
        return (0 <= row) &&
                (row < getRowCount()) &&
                (0 <= col) &&
                (col < getColCount());
    }

    @Override
    public ChessSquare getSquare(int index) { return _squares[index]; }

    @Override
    public ChessSquare getSquare(int row, int col) {
        if (squareExists(row, col)) {
            return _squares[row * getColCount() + col];
        } else {
            return null;
        }
    }

    @Override
    public ChessSquare getSquare(String notation) {
        notation = notation.trim().toLowerCase();

        if (notation.length() != 2)
            throw new IllegalArgumentException("Unexpected square notation: " + notation);

        char[] chars = notation.toCharArray();

        int col = (int) (chars[0] - 'a');
        int row = (int) (chars[1] - '1');

        return getSquare(row, col);
    }

    @Override
    public Set<ChessSquare> getSquares(String... notations) {
        Set<ChessSquare> squares = ChessFactory.createChessSquareSet();

        for (String notation : notations) {
            squares.add(getSquare(notation));
        }

        return squares;
    }

    @Override
    public Iterator<ChessSquare> iterator() {
        return new ChessSquareSet(this).iterator();
    }

    @Override
    public ChessPiece getPiece(int index) {
        return _pieces[index];
    }

    @Override
    public ChessPiece getPiece(PieceIndex index) {
        return getPiece(index.getValue());
    }

    public void setEnPassantSquare(ChessSquare enPassantSquare) {
        _enPassantSquare = enPassantSquare;

        if ((null != _enPassantSquare) && (null !=_enPassantSquare.getPiece())) {
            throw new InternalError("Occupied square " + _enPassantSquare + "can't be the en passant square");
        }
    }

    @Override
    public ChessSquare getEnPassantSquare() {
        if ((null != _enPassantSquare) && (null != _enPassantSquare.getPiece())) {
            throw new InternalError("Occupied square " + _enPassantSquare + "can't be the en passant square");
        }

        return _enPassantSquare;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (!(obj instanceof ChessBoard)) {
            return false;
        }

        ChessBoard other = (ChessBoard) obj;

        // Don't do the following ...
        // Instead consider two boards equal if the same pieces are in the same places ...
        /*
        if (null != this.getEnPassantSquare()) {
            if (!this.getEnPassantSquare().equals(other.getEnPassantSquare())) {
                return false;
            }
        } else if (null != other.getEnPassantSquare()) {
            return false;
        } */

        if (this.getRowCount() != other.getRowCount()) {
            return false;
        }

        if (this.getColCount() != other.getColCount()) {
            return false;
        }

        for (int row = 0; row < this.getRowCount(); ++row) {
            for (int col = 0; col < this.getColCount(); ++col) {
                if (!this.getSquare(row, col).equals(other.getSquare(row, col))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("    a   b   c   d   e   f   g   h\r\n");
        sb.append("  +---+---+---+---+---+---+---+---+\r\n");

        for (char row = '8'; row >= '1'; --row)
        {
            sb.append(String.format("%c |", row));

            for (char col = 'a'; col <= 'h'; ++col)
            {
                ChessSquare square = this.getSquare(String.format("%c%c", col, row));

                String notation;

                if (null == square.getPiece())
                    notation = " ";
                else {
                    notation = square.getPiece().notation();

                    if (square.getPiece().getSideColor() == SideColor.White) {
                        notation = notation.toUpperCase();
                    } else {
                        notation = notation.toLowerCase();
                    }
                }


                sb.append(String.format(" %s |", notation));
            }

            sb.append(String.format(" %c\r\n", row));
            sb.append("  +---+---+---+---+---+---+---+---+\r\n");
        }

        sb.append("    a   b   c   d   e   f   g   h\r\n");

        return sb.toString();

    }
}
