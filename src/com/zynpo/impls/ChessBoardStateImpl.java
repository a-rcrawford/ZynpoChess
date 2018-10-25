package com.zynpo.impls;

import com.zynpo.enums.*;
import com.zynpo.impls.pieces.BishopImpl;
import com.zynpo.impls.pieces.CastleImpl;
import com.zynpo.impls.pieces.KnightImpl;
import com.zynpo.impls.pieces.QueenImpl;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessBoardState;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.MoveRecord;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Pawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ChessBoardStateImpl implements ChessBoardState {

    private ChessBoard _board;
    private SideColor _sideToMove;
    private boolean _sideToMoveIsInCheck;
    private boolean _sideToMoveHasValidMove;
    private List<MoveRecord> _validMoves;
    private GameStatus _gameStatus;


    ChessBoardStateImpl(ChessBoard board, SideColor sideToMove, boolean findAllValidMoves) {
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

        if (findAllValidMoves) {
            _validMoves = new ArrayList<>();
        } else {
            _validMoves = null;
        }

        _sideToMoveHasValidMove = false;

        for (ChessPiece pieceToMove : piecesToMoveInPlay) {
            ChessSquare squareDeparted = pieceToMove.getSquare();

            for (ChessSquare squareOccupied : pieceToMove.potentialMoveSquares(PotentialMoveReason.ForNextMove)) {
                ChessPiece takenPiece = pieceToMove.moveToSquare(squareOccupied);
                boolean isValidMove = !sideToMoveKing.getSquare().coveredBy(opposingSideColor);
                pieceToMove.takeBackToSquare(squareDeparted, takenPiece);

                if (isValidMove) {
                    _sideToMoveHasValidMove = true;

                    if (findAllValidMoves) {
                        if ((pieceToMove instanceof Pawn) && (((Pawn) pieceToMove).promotionRow() == squareOccupied.getRow())) {
                            _validMoves.add(new MoveRecordImpl(pieceToMove,
                                    takenPiece, new KnightImpl((Pawn) pieceToMove), squareDeparted, squareOccupied,
                                    takenPiece.getSquare()));

                            _validMoves.add(new MoveRecordImpl(pieceToMove,
                                    takenPiece, new BishopImpl((Pawn) pieceToMove), squareDeparted, squareOccupied,
                                    takenPiece.getSquare()));

                            _validMoves.add(new MoveRecordImpl(pieceToMove,
                                    takenPiece, new CastleImpl((Pawn) pieceToMove), squareDeparted, squareOccupied,
                                    takenPiece.getSquare()));

                            _validMoves.add(new MoveRecordImpl(pieceToMove,
                                    takenPiece, new QueenImpl((Pawn) pieceToMove), squareDeparted, squareOccupied,
                                    takenPiece.getSquare()));
                        } else {
                            _validMoves.add(new MoveRecordImpl(pieceToMove,
                                    takenPiece, null, squareDeparted, squareOccupied,
                                    takenPiece.getSquare()));
                        }
                    } else {
                        break;
                    }
                }
            }

            if (_sideToMoveHasValidMove && !findAllValidMoves) {
                break;
            }
        }

        if (_sideToMoveHasValidMove && findAllValidMoves) {
            Collections.sort(_validMoves);
        }

        if (0 == this.getValidMoves().size()) {
            // The game is over for some reason, so determine why ...
            if (this.sideToMoveIsInCheck()) {
                if (this.getSideToMove() == SideColor.White) {
                    _gameStatus = GameStatus.BlackWinByCheckmate;
                } else {
                    _gameStatus = GameStatus.WhiteWinByCheckmate;
                }
            } else {
                _gameStatus = GameStatus.DrawByStalemate;
            }
        } else {
            _gameStatus = GameStatus.InPlay;

            Set<ChessPiece> allPiecesOtherThanKings = _board.getPiecesInPlay(PieceFlags.AllPiecesOtherThanKings);

            if (1 == allPiecesOtherThanKings.size()) {
                for (ChessPiece singlePiece : allPiecesOtherThanKings) {
                    if (!(singlePiece instanceof Pawn)) {
                        _gameStatus = GameStatus.DrawByInsufficientMaterial;
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (!(obj instanceof ChessBoardState)) {
            return false;
        }

        ChessBoardState other = (ChessBoardState) obj;

        if (!this.getBoard().equals(other.getBoard())) {
            return false;
        }

        if (this.getValidMoves().size() != other.getValidMoves().size()) {
            return false;
        }

        for (int i = 0; i < this.getValidMoves().size(); ++i) {
            if (!this.getValidMoves().get(i).equals(other.getValidMoves().get(i))) {
                return false;
            }
        }

        return true;
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
    public boolean sideToMoveHasValidMove() {
        return _sideToMoveHasValidMove;
    }

    @Override
    public List<MoveRecord> getValidMoves() {
        if (this.sideToMoveHasValidMove() && (null == _validMoves)) {
            throw new InternalError("Shouldn't call getValidMoves() ChessBoardState constructed with findAllValidMoves = false");
        }

        return _validMoves;
    }

    @Override
    public GameStatus getGameStatus() {
        return _gameStatus;
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

        switch (this.getGameStatus()) {
            case InPlay:
            case WhiteInCheck:
            case BlackInCheck:
                sb.append(this.getValidMoves().size() + " available moves: ");

                for (MoveRecord moveRecord : this.getValidMoves()) {
                    sb.append(moveRecord + " ");
                }

                break;
            default:
                sb.append("Final Game Status: " + this.getGameStatus());
        }

        return sb.toString();
    }

}
