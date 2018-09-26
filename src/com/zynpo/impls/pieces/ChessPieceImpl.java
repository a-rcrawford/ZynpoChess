package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessBoardImpl;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.pieces.*;
import com.zynpo.interfaces.ChessSquare;

import java.util.Set;


abstract class ChessPieceImpl implements ChessPiece {

    private SideColor _sideColor;
    private ChessBoard _board;
    private ChessSquare _square;
    private ChessSquare _origSquare;

    private PieceIndex _index;
    private int _movedCount;


    ChessPieceImpl(PieceIndex index, ChessSquare square) {
        this(
            index,
            square,
            index.origSideColor()
        );
    }


    ChessPieceImpl(PieceIndex index, ChessSquare square, SideColor sideColor) {
        _index = index;
        _movedCount = 0;
        _sideColor = sideColor;
        this.dropToSquare(square);
    }


    ChessPieceImpl(ChessPiece otherPiece, ChessSquare otherSquare) {
        _index = otherPiece.getIndex();
        _movedCount = otherPiece.getMovedCount();
        _sideColor = otherPiece.getSideColor();
        this.dropToSquare(otherSquare);
    }


    abstract protected String name();

    @Override
    public String toString() {
        if (null == this.getSquare()) {
            return "Taken " + this.getSideColor() + " " + name();
        } else {
            return "" + this.getSideColor() + " " + name() + " on " + this.getSquare();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (!(obj instanceof ChessPiece)) {
            return false;
        }

        ChessPiece other = (ChessPiece) obj;

        if (this.getSideColor() != other.getSideColor()) {
            return false;
        }

        if (!this.notation().equals(other.notation())) {
            return false;
        }

        return true;
    }

    @Override
    public ChessSquare getSquare() {
        return _square;
    }


    protected void setSquare(ChessSquare square) {
        if (square == _square) {
            return;
        }

        ChessSquare priorSquare = _square;
        _square = square;

        if (null != square) {
            if (null == _board) {
                _board = square.getBoard();
            }

            if (_square.getPiece() != this) {
                _square.setPiece(this);
            }
        }

        if ((null != priorSquare) && (priorSquare.getPiece() == this)) {
            priorSquare.setPiece(null);
        }

        if ((null != square) && (null != priorSquare)) {
            if (square.getBoard() != priorSquare.getBoard()) {
                throw new InternalError("Can't move " + this + " from one board to another");
            }
        }
    }

    @Override
    public void dropToSquare(ChessSquare square) {
        if (null == square) {
            throw new InternalError("Can't drop " + this + " onto null");
        }

        if (square.getPiece() != null) {
            throw new InternalError("Can't drop " + this + " onto "
                    + square + " that already contains " + square.getPiece());
        }

        _movedCount = 0;
        _origSquare = square;
        this.setSquare(square);
        ((ChessBoardImpl) _board).setEnPassantSquare(null);
    }


    @Override
    public ChessPiece moveToSquare(ChessSquare square) {
        if (null == this.getSquare()) {
            throw new InternalError("Can't move " + this + " from null to " + square);
        }

        if (null == square) {
            throw new InternalError("Can't move " + this + " to null square");
        }

        if (this.getSquare() == square) {
            throw new InternalError("Can't move " + this + " to the same square " + square);
        }

        ChessPieceImpl takenPiece = (ChessPieceImpl) square.getPiece();
        if (null != takenPiece) {
            takenPiece.setSquare(null);
        }

        this.setSquare(square);
        ++_movedCount;
        ((ChessBoardImpl) _board).setEnPassantSquare(null);

        return takenPiece;
    }


    @Override
    public void takeBackToSquare(ChessSquare square, ChessPiece formerlyTakenPiece) {
        if (null != this.getSquare()) {
            this.getSquare().setPiece(formerlyTakenPiece);
            --_movedCount;
        } else {
            if (null != formerlyTakenPiece) {
                throw new InternalError("How can " + this
                        + " have taken " + formerlyTakenPiece + " when it's not on a square?");
            }
        }

        if (square.getPiece() != null) {
            throw new InternalError("Can't take " + this
                    + " back to " + square + " when it already contains " + square.getPiece());
        }

        this.setSquare(square);
        ((ChessBoardImpl) _board).setEnPassantSquare(null);
    }

    @Override
    public ChessSquare getOrigSquare() { return _origSquare; }

    @Override
    public ChessBoard getBoard() { return _board; }

    @Override
    public SideColor getSideColor() { return _sideColor; }

    @Override
    public boolean onSameSideAs(ChessPiece other) { return this.getSideColor() == other.getSideColor(); }

    @Override
    public boolean opposesSideOf(ChessPiece other) { return this.getSideColor() != other.getSideColor(); }

    @Override
    public int getMovedCount() { return _movedCount; }

    @Override
    public PieceIndex getIndex() { return _index; }

    private boolean isDifferentSquareOnSameBoard(ChessSquare square) {
        if (null == this.getSquare()) {
            return false;
        }

        if (null == square) {
            return false;
        }

        if (this.getBoard() != square.getBoard()) {
            throw new IllegalArgumentException("Shouldn't be comparing a "
                    + this.name() + " from one board to a square on another.");
        }

        if (this.getSquare() == square) {
            // This is not a different square ...
            return false;
        }

        return true;
    }

    @Override
    public boolean covers(ChessSquare square) {
        if (!isDifferentSquareOnSameBoard(square)) {
            return false;
        }

        if (this instanceof Pawn) {
            if (this.getSquare().getRow() + ((Pawn) this).advanceUnit() == square.getRow()) {
                if (this.getSquare().colDistanceFrom(square) == 1) {
                    return true;
                }
            }
        }

        if ((this instanceof Bishop) || (this instanceof Queen)) {
            if (this.getSquare().rowDistanceFrom(square) == this.getSquare().colDistanceFrom(square)) {
                return this.coversStraightWay(square);
            }
        }

        if ((this instanceof Castle) || (this instanceof Queen)) {
            if (this.getSquare().getRow() == square.getRow()) {
                return this.coversStraightWay(square);
            }

            if (this.getSquare().getCol() == square.getCol()) {
                return this.coversStraightWay(square);
            }
        }

        if (this instanceof Knight) {
            if(3 == (this.getSquare().rowDistanceFrom(square) + this.getSquare().colDistanceFrom(square))) {
                if ((this.getSquare().getRow() != square.getRow())
                    && (this.getSquare().getCol() != square.getCol())) {
                    return true;
                }
            }
        }

        if (this instanceof King) {
            if (this.getSquare().colDistanceFrom(square) <= 1) {
                if (this.getSquare().rowDistanceFrom(square) <= 1) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean mightMoveTo(ChessSquare square) {
        if (!this.isDifferentSquareOnSameBoard(square)) {
            return false;
        }

        ChessPiece piece = square.getPiece();

        if (this.covers(square)) {
            if (null == piece) {
                if (this instanceof Pawn) {
                    if (this.getBoard().getEnPassantSquare() == square) {
                        // Pawns can only move onto empty covered squares when en-passant is possible ...
                        return true;
                    }
                } else {
                    // The rest of the pieces can generally move onto their covered squares that aren't occupied ...
                    return true;
                }
            } else if (this.opposesSideOf(piece)) {
                // All pieces can take an opposing piece on a square they cover ...
                return true;
            }
        }

        if (this instanceof Pawn) {
            if (null == piece) {
                Pawn pawn = (Pawn) this;
                // Pawns should always have a SquareJustInFront because they can't sit
                // on the PromotionRow without being promoted ...

                if (null == pawn.squareJustInFront().getPiece()) {

                    if (pawn.squareJustInFront() == square) {
                        return true;
                    }

                    if ((0 == this.getMovedCount()) && (pawn.jumpTwoSquare() == square)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean coversAnyOf(Iterable<ChessSquare> squares) {
        for (ChessSquare square : squares) {
            if (this.covers(square))
                return true;
        }

        return false;
    }

    /**
     * Does this piece connect to the given square because it is on the same
     * diagonal, horizontal or vertical, without being blocked by another piece?
     *
     * @param square
     * @return
     */
    private boolean coversStraightWay(ChessSquare square) {
        int rowStep = square.getRow() - this.getSquare().getRow();
        int colStep = square.getCol() - this.getSquare().getCol();

        if (0 != rowStep)
            rowStep /= Math.abs(rowStep);

        if (0 != colStep)
            colStep /= Math.abs(colStep);

        for(ChessSquare betweenSquare = this.getSquare().getRelativeSquare(rowStep, colStep);
            betweenSquare != square;
            betweenSquare = betweenSquare.getRelativeSquare(rowStep, colStep)) {

            if (null != betweenSquare.getPiece())
                return false; // Something is blocking this piece from connecting straightway to the square.
        }

        // If still here, nothing is blocking this Piece from connecting straightway to the square ...
        return true;
    }


    @Override
    public Set<ChessSquare> potentialMoveSquares(PotentialMoveReason reason) {
        final boolean movesLikeBishop = (this instanceof Bishop) || (this instanceof Queen);
        final boolean movesLikeCastle = (this instanceof Castle) || (this instanceof Queen);

        if (!(movesLikeBishop || movesLikeCastle)) {
            throw new InternalError(
                    "Shouldn't be calling ChessPieceImpl.potentialMoveSquares() " +
                    "for a " + this.name() + " that doesn't move like a bishop or a castle.");
        }

        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowDirection = -1; rowDirection <= 1; ++rowDirection ) {
            for (int colDirection = -1; colDirection <= 1; ++colDirection ) {

                if ((0 == rowDirection) && (0 == colDirection)) {
                    // Obviously this piece must move in some direction ...
                    continue;
                }

                if ((!movesLikeBishop) && (Math.abs(rowDirection) == Math.abs(colDirection))) {
                    // Don't check for potential diagonal moves if this piece doesn't move like a bishop ...
                    continue;
                }

                if ((!movesLikeCastle) && ((0 == rowDirection) || (0 == colDirection))) {
                    // Don't check for potential horizontal or vertical moves if this piece doesn't move like a castle ...
                    continue;
                }

                for (int step = 1; ; ++step) {
                    ChessSquare potential = this.getSquare().getRelativeSquare(rowDirection * step, colDirection * step);

                    if (null == potential) {
                        // We already ran off the end of the board ...
                        break;
                    }

                    ChessPiece piece = potential.getPiece();

                    if (null == piece) {
                        // Assume this piece can move to an empty Square ...
                        potentials.add(potential);
                    } else {
                        if (PotentialMoveReason.ForNextMove == reason) {
                            if (piece.opposesSideOf(this)) {
                                // Assume this piece can take an opposing piece ...
                                potentials.add(potential);
                            }

                            // Step no further in this direction, because this
                            // piece is blocked from here on ...
                            break;
                        } else if (PotentialMoveReason.ForMoveAfterNext == reason) {
                            // Only assume this piece won't land on, or move through, its own King
                            // for the move after next ...
                            if ((piece instanceof King) && piece.onSameSideAs(this)) {
                                break;
                            }

                            potentials.add(potential);
                        }
                    }
                }
            }
        }

        return potentials;
    }


}
