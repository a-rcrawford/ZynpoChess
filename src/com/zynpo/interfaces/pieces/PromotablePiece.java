package com.zynpo.interfaces.pieces;

/**
 * Pieces that may have been born out of promoted Pawns.
 */
public interface PromotablePiece extends ChessPiece {

    /**
     * @return the original pawn this piece was born out of, or null.
     */
    Pawn getOrigPawn();

}
