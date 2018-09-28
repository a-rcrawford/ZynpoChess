package com.zynpo.impls;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.PieceIndex;
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
import java.util.Set;

public class ChessBoardStateImpl implements ChessBoardState {

    private ChessBoard _board;
    private SideColor _sideToMove;
    private boolean _sideToMoveIsInCheck;

    private List<MoveRecord> _validMoves;


    ChessBoardStateImpl(ChessBoard board, SideColor sideToMove) {
        _board = board.clone();
        _sideToMove = sideToMove;

        King sideToMoveKing = null;
        Set<ChessPiece> piecesToMoveInPlay = null;
        Set<ChessPiece> opposingPiecesInPlay = null;

        switch (_sideToMove) {
            case White:
                sideToMoveKing = (King) _board.getPiecesInPlay(PieceFlags.WhiteKing).toArray()[0];
                piecesToMoveInPlay = _board.getPiecesInPlay(PieceFlags.AllWhitePieces);
                opposingPiecesInPlay = _board.getPiecesInPlay(PieceFlags.AllBlackPieces);
                break;
            case Black:
                sideToMoveKing = (King) _board.getPiecesInPlay(PieceFlags.BlackKing).toArray()[0];
                piecesToMoveInPlay = _board.getPiecesInPlay(PieceFlags.AllBlackPieces);
                opposingPiecesInPlay = _board.getPiecesInPlay(PieceFlags.AllWhitePieces);
                break;
            default:
                throw new InternalError("Can't construct ChessBoardState when sideToMove = " + sideToMove);
        }

        // Assume the side to move is not in check until proven otherwise ...
        _sideToMoveIsInCheck = false;

        for (ChessPiece opposingPiece : opposingPiecesInPlay) {
            if (opposingPiece.covers(sideToMoveKing.getSquare())) {
                _sideToMoveIsInCheck = true;
                break;
            }
        }

        _validMoves = new ArrayList<>();

        // TODO: Pick up from right here ...
        // Especially hard to figure out if the side to move king can castle ...
    }


    @Override
    public ChessBoard getBoard() {
        return _board;
    }

    @Override
    public SideColor getSideToMove() {
        return _sideToMove;
    }

    @Override
    public boolean sideToMoveIsInCheck() {
        return _sideToMoveIsInCheck;
    }

    @Override
    public List<MoveRecord> getValidMoves() {
        return _validMoves;
    }

}
