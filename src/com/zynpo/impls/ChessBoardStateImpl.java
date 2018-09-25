package com.zynpo.impls;

import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessBoardState;
import com.zynpo.interfaces.ChessSquare;

public class ChessBoardStateImpl /* implements ChessBoardState */ {

    private ChessBoard _board;
    private SideColor _sideToMove;

    private ChessPieceSet _piecesInPlay;


    ChessBoardStateImpl(ChessBoard board, SideColor sideToMove) {
        _board = board;
        _sideToMove = sideToMove;

        _piecesInPlay = new ChessPieceSet();

        for (ChessSquare square : board) {
            if (null != square.getPiece()) {
                _piecesInPlay.add(square.getPiece());
            }
        }
    }


    //@Override
    public ChessBoard getBoard() {
        return _board;
    }

}
