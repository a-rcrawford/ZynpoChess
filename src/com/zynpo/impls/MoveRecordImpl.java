package com.zynpo.impls;

import com.zynpo.enums.GameStatus;
import com.zynpo.enums.PieceFlags;
import com.zynpo.impls.pieces.PromotablePieceImpl;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.MoveRecord;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Pawn;
import com.zynpo.interfaces.pieces.PromotablePiece;

import java.util.Set;

public class MoveRecordImpl implements MoveRecord {

    private String _notation;

    private ChessPiece _pieceMoved;
    private ChessPiece _pieceTaken;
    private PromotablePiece _promotedToPiece;

    private ChessSquare _squareDeparted;
    private ChessSquare _squareOccupied;
    private ChessSquare _squareOfTakenPiece;

    private GameStatus _gameStatus;

    public MoveRecordImpl(ChessPiece pieceMoved,
                          ChessPiece pieceTaken,
                          PromotablePiece promotedToPiece,
                          ChessSquare squareDeparted,
                          ChessSquare squareOccupied,
                          ChessSquare squareOfTakenPiece,
                          GameStatus gameStatus) {

        _pieceMoved = pieceMoved;
        _pieceTaken = pieceTaken;
        _promotedToPiece = promotedToPiece;

        _squareDeparted = squareDeparted;
        _squareOccupied = squareOccupied;
        _squareOfTakenPiece = squareOfTakenPiece;

        _gameStatus = gameStatus;

        if (null == _gameStatus) {
            gameStatus = GameStatus.NotDetermined;
        }

        boolean mustDifferentiateRow = false;
        boolean mustDifferentiateCol = false;

        if (_pieceMoved instanceof Pawn) {
            Pawn pawn = (Pawn) _pieceMoved;

            if (_squareOccupied.getRow() == pawn.promotionRow()) {
                if (null == _promotedToPiece) {
                    // Put an abstract promotable piece in place if we don't know what it is yet ...
                    _promotedToPiece = new PromotablePieceImpl(pawn);
                }
            }

            if (null != pieceTaken) {
                // When a pawn takes something the column is always specified in the notation ...
                mustDifferentiateCol = true;
            }
        } else {
            _notation = _pieceMoved.notation();
        }

        int similarPieceFlags = PieceFlags.similarPiecesOfSameSide(pieceMoved);
        Set<ChessPiece> similarPieces = pieceMoved.getBoard().getPiecesInPlay(similarPieceFlags);

        for (ChessPiece similarPiece : similarPieces) {
            if (similarPiece.mightMoveTo(squareOccupied)) {
                if (similarPiece.getSquare().getCol() != squareDeparted.getCol()) {
                    mustDifferentiateCol = true;
                } else if (similarPiece.getSquare().getRow() != squareDeparted.getRow()) {
                    mustDifferentiateRow = true;
                } else {
                    throw new InternalError(similarPiece + " similar to " + pieceMoved + " might move to " + squareOccupied + "?");
                }
            }
        }

        if (mustDifferentiateCol) {
            _notation += squareDeparted.getCol();
        }

        if (mustDifferentiateRow) {
            _notation += squareDeparted.getRow();
        }

        if (null == pieceTaken) {
            _notation += squareOccupied;
        } else {
            _notation += "x" + squareOfTakenPiece;

            if (!_squareOfTakenPiece.equals(squareOccupied)) {
                _notation += "ep";
            }
        }

        if (null != promotedToPiece) {
            _notation += "=" + promotedToPiece.notation();
        }

        if (_pieceMoved instanceof King) {
            if (2 == _squareOccupied.colsAwayFromCount(_squareDeparted)) {
                _notation = "O-O";
            } else if (-2 == squareOccupied.colsAwayFromCount(_squareDeparted)) {
                _notation = "O-O-O";
            }
        }

        switch (_gameStatus) {
            case WhiteInCheck:
            case BlackInCheck:
                _notation += "+";
                break;
            case WhiteWinByCheckmate:
            case BlackWinByCheckmate:
                _notation += "++";
                break;
        }
    }


    @Override
    public String notation() {
        return _notation;
    }


    @Override
    public String toString() {
        return notation();
    }


    @Override
    public ChessPiece pieceMoved() {
        return _pieceMoved;
    }


    @Override
    public PromotablePiece promotedToPiece() {
        return _promotedToPiece;
    }


    @Override
    public ChessPiece pieceTaken() {
        return _pieceTaken;
    }


    @Override
    public ChessSquare squareDeparted() {
        return _squareDeparted;
    }


    @Override
    public ChessSquare squareOccupied() {
        return _squareOccupied;
    }


    @Override
    public ChessSquare squareOfTakenPiece() {
        return _squareOfTakenPiece;
    }
}
