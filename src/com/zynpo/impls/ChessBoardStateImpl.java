package com.zynpo.impls;

import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessBoardState;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.MoveRecord;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardStateImpl /* implements ChessBoardState */ {

    private ChessBoard _board;
    private SideColor _sideToMove;

    private ChessPieceSet _piecesInPlay;

    private List<MoveRecord> _validMoves;


    ChessBoardStateImpl(ChessBoard board, SideColor sideToMove) {
        _board = board;
        _sideToMove = sideToMove;

        _piecesInPlay = new ChessPieceSet();

        King king = null;

        for (ChessSquare square : board) {
            ChessPiece piece = square.getPiece();
            if (null != piece) {
                _piecesInPlay.add(piece);

                if ((piece instanceof King) && (piece.getSideColor() == sideToMove)) {
                    king = (King) piece;
                }
            }
        }

        board = _board.clone();
        ChessPieceSet piecesInPlay = _piecesInPlay.clone(board);

        _validMoves = new ArrayList<>();

        for (ChessPiece piece : _piecesInPlay) {
            for (ChessSquare square : piece.potentialMoveSquares(PotentialMoveReason.ForNextMove)) {

            }
        }
    }


    //@Override
    public ChessBoard getBoard() {
        return _board;
    }

}
