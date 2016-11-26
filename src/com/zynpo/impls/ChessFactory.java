package com.zynpo.impls;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.PieceIndex;
import com.zynpo.impls.pieces.CastleImpl;
import com.zynpo.impls.pieces.PawnImpl;
import com.zynpo.interfaces.*;
import com.zynpo.interfaces.pieces.ChessPiece;

import java.util.Set;


public class ChessFactory {

    private ChessFactory() {}

    static ChessSquare createSquare(ChessBoard board, int row, int col) {
        return new ChessSquareImpl(board, row, col);
    }

    public static ChessBoard createBoard() {
        return new ChessBoardImpl();
    }


    public static Set<ChessSquare> createChessSquareSet(ChessSquare... squares) {
        Set<ChessSquare> chessSquareSet = new ChessSquareSet();

        for (int i = 0; i < squares.length; ++i)
            chessSquareSet.add(squares[i]);

        return chessSquareSet;
    }


    public static Set<ChessPiece> createChessPieceSet(ChessPiece... pieces) {
        Set<ChessPiece> chessPieceSet = new ChessPieceSet();

        for (int i = 0; i < pieces.length; ++i)
            chessPieceSet.add(pieces[i]);

        return chessPieceSet;
    }

}
