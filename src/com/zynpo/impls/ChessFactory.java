package com.zynpo.impls;

import com.zynpo.interfaces.*;

import java.util.Set;


public class ChessFactory {

    private ChessFactory() {}

    static ChessSquare createSquare(ChessBoard board, int row, int col) {
        return new ChessSquareImpl(board, row, col);
    }

    public static ChessBoard createBoard() {
        return new ChessBoardImpl();
    }

    public static Set<ChessSquare> createChessSquareSet() { return new ChessSquareSet(); }

}
