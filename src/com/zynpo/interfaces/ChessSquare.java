package com.zynpo.interfaces;

import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.pieces.ChessPiece;


public interface ChessSquare extends Comparable<ChessSquare> {

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

    /**
     * @return the ChessPiece occupying this Square, or null for no Piece.
     */
    ChessPiece getPiece();

    /**
     * Set this Square's piece to the given parameter
     * @param piece to occupy this Square
     * @return the piece that was occupying this Square prior
     */
    void setPiece(ChessPiece piece);

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

    ChessSquare clone(ChessBoard otherBoard);

    boolean coveredBy(SideColor sideColor);
}
