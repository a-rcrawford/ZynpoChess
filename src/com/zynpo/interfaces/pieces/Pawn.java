package com.zynpo.interfaces.pieces;

import com.zynpo.interfaces.ChessSquare;

public interface Pawn extends ChessPiece {

    int advanceUnit();
    int gameStartRow();
    int jumpTwoRow();
    int promotionRow();

    ChessSquare squareJustInFront();
    ChessSquare jumpTwoSquare();

    PromotablePiece getPromotedToPiece();
    void setPromotedToPiece(PromotablePiece piece);
}
