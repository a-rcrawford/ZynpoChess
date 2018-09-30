package com.zynpo.impls;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.SideColor;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.MoveRecord;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.pieces.King;
import com.zynpo.interfaces.pieces.Pawn;

public class MoveRecordImpl implements MoveRecord {

    private String _notation;

    private ChessPiece _pieceMoved;
    private ChessPiece _pieceTaken;

    private ChessSquare _squareDeparted;
    private ChessSquare _squareOccupied;
    private ChessSquare _squareOfTakenPiece;

    public MoveRecordImpl(ChessPiece pieceMoved,
                          ChessPiece pieceTaken,
                          ChessSquare squareDeparted,
                          ChessSquare squareOccupied,
                          ChessSquare squareOfTakenPiece) {

        _pieceMoved = pieceMoved;
        _pieceTaken = pieceTaken;

        _squareDeparted = squareDeparted;
        _squareOccupied = squareOccupied;
        _squareOfTakenPiece = squareOfTakenPiece;

        if (!(_pieceMoved instanceof Pawn)) {
            _notation = _pieceMoved.notation();
        }

        if (null != _pieceTaken) {
            _notation += "x" + squareOfTakenPiece;

            if (!_squareOfTakenPiece.equals(squareOccupied)) {
                _notation += "ep";
            }
        }

        if (_pieceMoved instanceof King) {
            if (2 == _squareOccupied.colsAwayFromCount(_squareDeparted)) {
                _notation = "O-O";
            } else if (-2 == squareOccupied.colsAwayFromCount(_squareDeparted)) {
                _notation = "O-O-O";
            }
        }

        if (pieceMoved.getSideColor() == SideColor.White) {
            King opposingKing = (King) pieceMoved.getBoard().getPiecesInPlay(PieceFlags.BlackKing).toArray()[0];
            if (opposingKing.getSquare().coveredBy(SideColor.White)) {
                _notation += "+";
            }
        } else {
            King opposingKing = (King) pieceMoved.getBoard().getPiecesInPlay(PieceFlags.WhiteKing).toArray()[0];
            if (opposingKing.getSquare().coveredBy(SideColor.Black)) {
                _notation += "+";
            }
        }

        // TODO: Append another + to signify checkmate if the opposingKing can't get out of check.
    }


    @Override
    public String notation() {
        return _notation;
    }


    @Override
    public ChessPiece pieceMoved() {
        return _pieceMoved;
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
