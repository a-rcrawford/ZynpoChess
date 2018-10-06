package com.zynpo.impls;

import com.zynpo.enums.GameStatus;
import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.SideColor;
import com.zynpo.exceptions.AmbiguousMoveException;
import com.zynpo.exceptions.InvalidMoveException;
import com.zynpo.exceptions.MoveException;
import com.zynpo.impls.pieces.PromotablePieceImpl;
import com.zynpo.interfaces.ChessBoard;
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


    public MoveRecordImpl(String notation, SideColor sideToMove, ChessBoard board) throws MoveException {
        if (null == notation) {
            throw new IllegalArgumentException("Can't construct MoveRecord out of null");
        }

        notation = notation.trim();
        final String origNotation = notation;

        if (notation.length() == 0) {
            throw new IllegalArgumentException("Can't construct MoveRecord out of empty string");
        }

        try {

            GameStatus expectedGameStatus;

            if (notation.endsWith("++")) {
                expectedGameStatus = (SideColor.White == sideToMove ?
                        GameStatus.BlackWinByCheckmate : GameStatus.WhiteWinByCheckmate);
                notation = notation.substring(0, notation.length() - 3);
            } else if (notation.endsWith("#")) {
                expectedGameStatus = (SideColor.White == sideToMove ?
                        GameStatus.BlackWinByCheckmate : GameStatus.WhiteWinByCheckmate);
                notation = notation.substring(0, notation.length() - 2);
            } else if (notation.endsWith("+")) {
                expectedGameStatus = (SideColor.White == sideToMove ?
                        GameStatus.WhiteInCheck : GameStatus.BlackInCheck);
                notation = notation.substring(0, notation.length() - 2);
            } else {
                expectedGameStatus = GameStatus.InPlay;
            }

            boolean enPassant = false;

            if (notation.endsWith("ep")) {
                enPassant = true;
                notation = notation.substring(0, notation.length() - 3);
            }

            int pieceToMoveFlags = PieceFlags.piecesOfSameSide(PieceFlags.AllPawns, sideToMove);

            if (notation.startsWith("R") || notation.startsWith("C")) {
                pieceToMoveFlags = PieceFlags.piecesOfSameSide(PieceFlags.AllCastles, sideToMove);
                notation = notation.substring(1); // Trim the piece char away from the front.
            } else if (notation.startsWith("N")) {
                pieceToMoveFlags = PieceFlags.piecesOfSameSide(PieceFlags.AllKnights, sideToMove);
                notation = notation.substring(1);
            } else if (notation.startsWith("B")) {
                pieceToMoveFlags = PieceFlags.piecesOfSameSide(PieceFlags.AllBishops, sideToMove);
                notation = notation.substring(1);
            } else if (notation.startsWith("Q")) {
                pieceToMoveFlags = PieceFlags.piecesOfSameSide(PieceFlags.AllQueens, sideToMove);
                notation = notation.substring(1);
            } else if (notation.startsWith("K")) {
                pieceToMoveFlags = PieceFlags.piecesOfSameSide(PieceFlags.BothKings, sideToMove);
                notation = notation.substring(1);
            }

            String startCol = "";
            String startRow = "";

            if (('a' <= notation.charAt(0)) && (notation.charAt(0) <= 'a' + (board.getColCount() - 1))) {
                startCol = notation.substring(0, 0);
                notation = notation.substring(1);
            }

            if (('1' <= notation.charAt(0)) && (notation.charAt(0) <= '1' + (board.getRowCount() - 1))) {
                startRow = notation.substring(0, 0);
                notation = notation.substring(1);
            }

            boolean pieceTaken = (notation.charAt(0) == 'x');

            if (pieceTaken) {
                notation = notation.substring(1);
            } else if (enPassant) {
                throw new InvalidMoveException("Must x = take something to do ep = en-passant: " + origNotation);
            }

            if (enPassant) {
                if (!PieceFlags.AllPawns.contains(pieceToMoveFlags)) {
                    throw new InvalidMoveException("Only a pawn can take ep = en-passant: " + origNotation);
                }
            }

            // TODO: Detect Pawn promotions ...

            ChessSquare squareOccupied;
            ChessSquare squareOfTakenPiece = null;

            {
                ChessSquare likelySquareOccupied;

                String squareStr = notation.substring(notation.length() - 3);
                likelySquareOccupied = board.getSquare(squareStr);

                if (null == likelySquareOccupied) {
                    throw new InvalidMoveException("Can't determine the meaning of square: " + squareStr);
                }

                if (enPassant) {
                    squareOccupied = board.getEnPassantSquare();
                    squareOfTakenPiece = likelySquareOccupied;
                } else {
                    squareOccupied = likelySquareOccupied;
                    if (pieceTaken) {
                        squareOfTakenPiece = squareOccupied;
                    }
                }
            }

            Set<ChessPiece> possiblePiecesToMove = board.getPiecesInPlay(pieceToMoveFlags);

            if (possiblePiecesToMove.isEmpty()) {
                throw new InvalidMoveException("No piece found to make such a move: " + origNotation);
            }

            Set<ChessPiece> possiblePiecesToMove2 = ChessFactory.createChessPieceSet();

            for (ChessPiece possiblePieceToMove : possiblePiecesToMove) {
                if (possiblePieceToMove.mightMoveTo(squareOccupied)) {
                    if (startCol.isEmpty() || possiblePieceToMove.getSquare().toString().startsWith(startCol)) {
                        if (startRow.isEmpty() || possiblePieceToMove.getSquare().toString().endsWith(startRow)) {
                            possiblePiecesToMove2.add(possiblePieceToMove);
                        }
                    }
                }
            }

            if (possiblePiecesToMove2.isEmpty()) {
                throw new InvalidMoveException("Couldn't find piece to move to " + squareOccupied + ": " + origNotation);
            }

            if (1 < possiblePiecesToMove2.size()) {
                // TODO: Consider checking whether only one is a valid move ...
                throw new AmbiguousMoveException("More than one piece fits move description: " + origNotation);
            }

            ChessPiece pieceMoved = null;

            for (ChessPiece possiblePieceToMove : possiblePiecesToMove2) {
                pieceMoved = possiblePieceToMove;
            }

            this(pieceMoved,
                    pieceTaken,
                    )

        } catch (StringIndexOutOfBoundsException sioobe) {
            throw new InvalidMoveException(origNotation + " is an invalid move", sioobe);
        }
    }


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
