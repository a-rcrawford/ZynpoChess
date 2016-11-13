package com.zynpo.impls;

import com.zynpo.interfaces.*;


public class ChessFactory {

    private ChessFactory() {}

    static ChessSquare createSquare(ChessBoard board, int row, int col) {
        return new ChessSquareImpl(board, row, col);
    }

    public static ChessBoard createBoard() {
        return new ChessBoardImpl();
    }

}