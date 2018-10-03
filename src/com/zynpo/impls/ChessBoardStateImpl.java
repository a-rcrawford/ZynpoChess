package com.zynpo.impls;

import com.zynpo.enums.*;
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
            ChessSquare departedSquare = pieceToMove.getSquare();

            for (ChessSquare occupiedSquare : pieceToMove.potentialMoveSquares(PotentialMoveReason.ForNextMove)) {
                ChessPiece takenPiece = pieceToMove.moveToSquare(occupiedSquare);
                boolean isValidMove = !sideToMoveKing.getSquare().coveredBy(opposingSideColor);
                pieceToMove.takeBackToSquare(departedSquare, takenPiece);

                if (isValidMove) {
                    _validMoves.add(new MoveRecordImpl(pieceToMove,
                            takenPiece, null, departedSquare, occupiedSquare,
                            takenPiece.getSquare(), GameStatus.NotDetermined));
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getBoard() + "\r\n");
        sb.append("Taken white pieces: ");

        for (ChessPiece piece : getBoard().getPiecesOutOfPlay(PieceFlags.AllWhitePieces)) {
            sb.append(piece + " ");
        }

        sb.append("\r\nTaken black pieces: ");

        for (ChessPiece piece : getBoard().getPiecesOutOfPlay(PieceFlags.AllBlackPieces)) {
            sb.append(piece + " ");
        }

        sb.append("\r\nPlayer to move: " + this.getSideToMove() + "\r\n");
        sb.append(this.getValidMoves().size() + " available moves: ");

        for (MoveRecord moveRecord : this.getValidMoves()) {
            sb.append(moveRecord + " ");
        }

        return sb.toString();
    }

}
