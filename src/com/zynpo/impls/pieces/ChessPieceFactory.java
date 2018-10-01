package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.PieceIndex;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.ChessPiece;

public class ChessPieceFactory {

    private ChessPieceFactory() {}

    public static ChessPiece createPiece(PieceIndex index, ChessBoard board) {
        ChessSquare square = board.getSquare(index.origRow(), index.origCol());

        if (PieceFlags.AllPawns.contains(index)) {
            return new PawnImpl(index, square);
        } else if (PieceFlags.AllCastles.contains(index)) {
            return new CastleImpl(index, square);
        } else if (PieceFlags.AllKnights.contains(index)) {
            return new KnightImpl(index, square);
        } else if (PieceFlags.AllBishops.contains(index)) {
            return new BishopImpl(index, square);
        } else if (PieceFlags.AllQueens.contains(index)) {
            return new QueenImpl(index, square);
        } else if (PieceFlags.BothKings.contains(index)) {
            return new KingImpl(index, square);
        } else {
            throw new InternalError("PieceIndex not handled correctly");
        }
    }

    public static ChessPiece createPiece(int index, ChessBoard board) {
        return createPiece(PieceIndex.fromOrdinal(index), board);
    }
}
