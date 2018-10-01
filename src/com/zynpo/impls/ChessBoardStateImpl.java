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
        SideColor opposingSideColor;

        King sideToMoveKing = null;
        Set<ChessPiece> piecesToMoveInPlay = null;
        Set<ChessPiece> opposingPiecesInPlay = null;

        switch (_sideToMove) {
            case White:
                sideToMoveKing = (King) _board.getPiecesInPlay(PieceFlags.WhiteKing).toArray()[0];
                piecesToMoveInPlay = _board.getPiecesInPlay(PieceFlags.AllWhitePieces);
                opposingPiecesInPlay = _board.getPiecesInPlay(PieceFlags.AllBlackPieces);
                opposingSideColor = SideColor.Black;
                break;
            case Black:
                sideToMoveKing = (King) _board.getPiecesInPlay(PieceFlags.BlackKing).toArray()[0];
                piecesToMoveInPlay = _board.getPiecesInPlay(PieceFlags.AllBlackPieces);
                opposingPiecesInPlay = _board.getPiecesInPlay(PieceFlags.AllWhitePieces);
                opposingSideColor = SideColor.White;
                break;
            default:
                throw new InternalError("Can't construct ChessBoardState when sideToMove = " + sideToMove);
        }

        _sideToMoveIsInCheck = sideToMoveKing.getSquare().coveredBy(opposingSideColor);

        _validMoves = new ArrayList<>();

        for (ChessPiece pieceToMove : piecesToMoveInPlay) {
            for (ChessSquare square : pieceToMove.potentialMoveSquares(PotentialMoveReason.ForNextMove)) {
                ChessPiece takenPiece = pieceToMove.moveToSquare(square);

                if (!sideToMoveKing.getSquare().coveredBy(opposingSideColor)) {
                    //_validMoves.add(ChessFactory.createMoveRecord());
                }
            }
        }
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
