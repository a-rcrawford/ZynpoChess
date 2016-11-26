package com.zynpo.interfaces;

import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.pieces.ChessPiece;


public interface ChessSquare {

    int getIndex();

    /**
     * @return the row this Square sits on.
     */
    int getRow();

    /**
     * @return the column this Square sits on.
     */
    int getCol();

    /**
     * @return the Color of this Square sits on.
     */
    SideColor getSideColor();

    ChessBoard getBoard();

    boolean isOccupied();
    boolean isUnoccupied();
    ChessPiece getPiece();

    /**
     * Set this Square's piece to the given parameter
     * @param piece to occupy this Square
     * @return the piece that was occupying this Square prior
     */
    ChessPiece setPiece(ChessPiece piece);

    ChessSquare getRelativeSquare(int rowOffset, int colOffset);

    /**
     * @param other square to relate this square's row with
     * @return a positive or negative value indicating how many rows
     * away the other square sits.
     */
    int rowsAwayFromCount(ChessSquare other);
    int colsAwayFromCount(ChessSquare other);

    /**
     * @param other square to relate this square's row with
     * @return a positive int indicating how many rows
     * away the other square sits.
     */
    int rowDistanceFrom(ChessSquare other);
    int colDistanceFrom(ChessSquare other);


    static boolean canCompare(ChessSquare first, ChessSquare second) {
        if (null == first)
            return false;

        if (null == second)
            return false;

        if (first.getBoard() != second.getBoard()) {
            throw new IllegalArgumentException("Shouldn't be comparing Squares from different Boards");
        }

        if (first == second) {
            return false; // Shouldn't be comparing a Square with itself
        }

        return true;
    }


    static boolean onSameRow(ChessSquare first, ChessSquare second) {
        return canCompare(first, second) && (first.getRow() == second.getRow());
    }

    static boolean onSameCol(ChessSquare first, ChessSquare second) {
        return canCompare(first, second) && (first.getCol() == second.getCol());
    }

    static boolean onSameDiagonal(ChessSquare first, ChessSquare second) {
        return canCompare(first, second) &&
                (first.rowDistanceFrom(second) == first.colDistanceFrom(second));
    }
}
