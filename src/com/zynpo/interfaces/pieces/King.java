package com.zynpo.interfaces.pieces;

import com.zynpo.interfaces.ChessSquare;


public interface King extends ChessPiece {
    Castle leftCastle();
    Castle rightCastle();

    ChessSquare castleLeftSquare();
    ChessSquare castleRightSquare();
}
