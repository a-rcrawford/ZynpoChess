package com.zynpo.interfaces.pieces;

import com.zynpo.interfaces.ChessSquare;

public interface Castle extends PromotablePiece {
    ChessSquare castleWithKingDestinationSquare();
}
