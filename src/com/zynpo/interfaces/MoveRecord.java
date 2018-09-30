package com.zynpo.interfaces;

import com.zynpo.interfaces.pieces.ChessPiece;

public interface MoveRecord {
    String notation(); // Nf3, Bb5, etc.

    ChessPiece pieceMoved(); // Contains new piece in case of promotion
    ChessPiece pieceTaken(); // null if no piece was taken

    ChessSquare squareDeparted(); // null if pieceMoved was dropped into play
    ChessSquare squareOccupied(); // Square the pieceMoved landed on
    ChessSquare squareOfTakenPiece(); // In case of "en passant"
}
