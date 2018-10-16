package com.zynpo.interfaces;

import com.zynpo.enums.GameStatus;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.PromotablePiece;

public interface MoveRecord {
    String notation(); // Nf3, Bb5, etc.

    ChessPiece pieceMoved(); // Contains new piece in case of promotion
    ChessPiece pieceTaken(); // null if no piece was taken
    PromotablePiece promotedToPiece(); // What was the Pawn moved promoted to?

    ChessSquare squareDeparted(); // null if pieceMoved was dropped into play
    ChessSquare squareOccupied(); // Square the pieceMoved landed on
    ChessSquare squareOfTakenPiece(); // In case of "en passant"

    GameStatus gameStatus();
    ChessBoardState resultingBoardState();
}
