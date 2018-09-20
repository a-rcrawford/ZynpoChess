package com.zynpo.impls.pieces;

import com.zynpo.enums.PieceIndex;
import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessBoardImpl;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.pieces.ChessPiece;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.King;

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
        this.setSquare(square);
    }

    abstract protected String name();

    protected boolean movesLikeCastle() {
        return false;
    }

    protected boolean movesLikeBishop() {
        return false;
    }

    protected boolean movesLikeKnight() {
        return false;
    }

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

    @Override
    public boolean covers(ChessSquare square) {
        if (!(this.movesLikeBishop() || this.movesLikeCastle() || this.movesLikeKnight())) {
            throw new InternalError(
                    "Shouldn't be calling ChessPieceImpl.covers() " +
                    "for a piece that doesn't move like a bishop, castle, or knight.");
        }

        if (null == this.getSquare()) {
            return false;
        }

        if (null == square) {
            return false;
        }

        if (this.getBoard() != square.getBoard()) {
            throw new IllegalArgumentException("Shouldn't be asking whether a piece from one board covers a square on another.");
        }

        if (this.getSquare() == square) {
            return false;
        }

        if (this.movesLikeBishop()) {
            if (this.getSquare().rowDistanceFrom(square) == this.getSquare().colDistanceFrom(square)) {
                return this.coversStraightWay(square);
            }
        }

        if (this.movesLikeCastle()) {
            if (this.getSquare().getRow() == square.getRow()) {
                return this.coversStraightWay(square);
            }

            if (this.getSquare().getCol() == square.getCol()) {
                return this.coversStraightWay(square);
            }
        }

        if (this.movesLikeKnight()) {
            if(3 == (this.getSquare().rowDistanceFrom(square) + this.getSquare().colDistanceFrom(square))) {
                if ((this.getSquare().getRow() != square.getRow())
                    && (this.getSquare().getCol() != square.getCol())) {
                    return true;
                }
            }
        }

        return false;
    }

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
        if (!(this.movesLikeBishop() || this.movesLikeCastle())) {
            throw new InternalError(
                    "Shouldn't be calling ChessPieceImpl.potentialMoveSquares() " +
                    "for a piece that doesn't move like a bishop or a castle.");
        }

        Set<ChessSquare> potentials = ChessFactory.createChessSquareSet();

        for(int rowDirection = -1; rowDirection <= 1; ++rowDirection ) {
            for (int colDirection = -1; colDirection <= 1; ++colDirection ) {

                if ((0 == rowDirection) && (0 == colDirection)) {
                    // Obviously this piece must move in some direction ...
                    continue;
                }

                if ((!this.movesLikeBishop()) && (Math.abs(rowDirection) == Math.abs(colDirection))) {
                    // Don't check for potential diagonal moves if this piece doesn't move like a bishop ...
                    continue;
                }

                if ((!this.movesLikeCastle()) && ((0 == rowDirection) || (0 == colDirection))) {
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
