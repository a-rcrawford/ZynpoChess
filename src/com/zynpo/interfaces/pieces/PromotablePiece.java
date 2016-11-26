package com.zynpo.interfaces.pieces;

/**
 * Pieces that may have been born out of promoted Pawns.
 */
public interface PromotablePiece extends ChessPiece {

    Pawn getOrigPawn();

}
