package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessBoardImpl;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.ChessSquare;


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
        this.setSquare(square);
    }

    abstract protected String name();

    @Override
    public String toString() {
        if (null == this.getSquare()) {
            return "Taken " + _sideColor + " " + name();
        } else {
            return "" + _sideColor + " " + name() + " on " + this.getSquare();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (!(obj instanceof ChessPieceImpl)) {
            return false;
        }

        ChessPieceImpl other = (ChessPieceImpl) obj;

        if (this.getSideColor() != other.getSideColor()) {
            return false;
        }

        if (!this.name().equals(other.name())) {
            return false;
        }

        return true;
    }

    @Override
    public ChessSquare getSquare() {
        return _square;
    }

    @Override
    public ChessSquare setSquare(ChessSquare square) {
        if (square == _square) {
            return _square;
        }

        if (null != _board) {
            ((ChessBoardImpl) _board).setEnPassantSquare(null);
        }

        ChessSquare priorSquare = _square;
        _square = square;

        if (null != square) {
            if (null == _board) {
                _board = square.getBoard();
            }

            if (null == _origSquare) {
                _origSquare = _square;
            }

            if (_square.getPiece() != this) {
                _square.setPiece(this);
            }
        }

        if ((null != priorSquare) && (priorSquare.getPiece() == this)) {
            priorSquare.setPiece(null);
        }

        if ((null != square) && (null != priorSquare)) {
            // If we just moved from a prior square to a new one,
            // that means this piece has been moved in the game ...
            ++_movedCount;

            if (square.getBoard() != priorSquare.getBoard()) {
                throw new InternalError("Can't move " + this + " from one board to another");
            }
        }

        return priorSquare;
    }

    @Override
    public ChessSquare getOrigSquare() { return _origSquare; }

    @Override
    public ChessBoard getBoard() { return _board; }

    @Override
    public SideColor getSideColor() { return _sideColor; }

    @Override
    public SideColor getOpposingSideColor() { return _sideColor.opposingSideColor(); }

    @Override
    public boolean onSameSideAs(ChessPiece other) { return this.getSideColor() == other.getSideColor(); }

    @Override
    public boolean opposesSideOf(ChessPiece other) { return this.getSideColor() != other.getSideColor(); }

    @Override
    public int getMovedCount() { return _movedCount; }

    @Override
    public PieceIndex getIndex() { return _index; }

    @Override // This works fine for all Pieces other than Kings and Pawns ...
    public boolean mightMoveTo(ChessSquare square) {
        if (this.covers(square)) {
            ChessPiece piece = square.getPiece();
            if ((null == piece) || this.opposesSideOf(piece)) {
                return true;
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


    private boolean coversStraightWay(ChessSquare square) {
        int rowStep = square.getRow() - _square.getRow();
        int colStep = square.getCol() - _square.getCol();

        if (0 != rowStep)
            rowStep /= Math.abs(rowStep);

        if (0 != colStep)
            colStep /= Math.abs(colStep);

        for(ChessSquare betweenSquare = _square.getRelativeSquare(rowStep, colStep);
            betweenSquare != square;
            betweenSquare = betweenSquare.getRelativeSquare(rowStep, colStep)) {

            if (null != betweenSquare.getPiece())
                return false; // Something is blocking this piece from connecting straightway to the square.
        }

        // If still here, nothing is blocking this Piece from connecting straightway to the square ...
        return true;
    }


    protected boolean coversLikeCastle(ChessSquare square) {
        if (square == _square) {
            return false;
        }

        if (ChessSquare.onSameRow(_square, square) || ChessSquare.onSameCol(_square, square)) {
            return coversStraightWay(square);
        }

        return false;
    }


    protected  boolean coversLikeBishop(ChessSquare square) {
        if (square == _square) {
            return false;
        }

        if (ChessSquare.onSameDiagonal(_square, square)) {
            return coversStraightWay(square);
        }

        return false;
    }


    protected boolean coversLikeKnight(ChessSquare square) {
        if (square == _square) {
            return false;
        }

        if (!ChessSquare.canCompare(_square, square)) {
            return false;
        }

        if (!ChessSquare.onSameRow(_square, square)) {
            if (!ChessSquare.onSameCol(_square, square)) {
                return 3 == (_square.rowDistanceFrom(square) + _square.colDistanceFrom(square));
            }
        }

        return false;
    }

}
