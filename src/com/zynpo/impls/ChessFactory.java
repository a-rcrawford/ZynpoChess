package com.zynpo.impls;

import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.MoveRecord;
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

        for (ChessSquare square : squares)
            chessSquareSet.add(square);

        return chessSquareSet;
    }


    public static Set<ChessPiece> createChessPieceSet(ChessPiece... pieces) {
        Set<ChessPiece> chessPieceSet = new ChessPieceSet();

        for (ChessPiece piece : pieces)
            chessPieceSet.add(piece);

        return chessPieceSet;
    }


    //public static MoveRecord createMoveRecord()

}
