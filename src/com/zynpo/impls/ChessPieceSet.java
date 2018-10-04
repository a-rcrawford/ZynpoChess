package com.zynpo.impls;

import java.lang.reflect.Array;
import java.util.*;

import com.zynpo.enums.PieceFlags;
import com.zynpo.enums.PieceIndex;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.pieces.*;


class ChessPieceSet implements Set<ChessPiece> {

    int _bitMask;
    ChessBoard _board;


    ChessPieceSet() {
        _bitMask = 0;
        _board = null;
    }


    ChessPieceSet(ChessPieceSet chessPieceSet, ChessBoard board) {
        _bitMask = chessPieceSet._bitMask;

        if (0 != _bitMask) {
            _board = board;
        }
    }

    public ChessPieceSet(int bitMask, ChessBoard board) {
        _bitMask = bitMask;
        _board = board;

        boolean pawnsDesired = PieceFlags.AllPawns.containsAnyOf(_bitMask);
        boolean castlesDesired = PieceFlags.AllCastles.containsAnyOf(_bitMask);
        boolean knightsDesired = PieceFlags.AllKnights.containsAnyOf(_bitMask);
        boolean bishopsDesired = PieceFlags.AllBishops.containsAnyOf(_bitMask);
        boolean queensDesired = PieceFlags.AllQueens.containsAnyOf(_bitMask);

        // Pawns in play might have been promoted to other PromotablePieces, which complicates things ...
        for (int bitIndex = PieceIndex.WhitePawnA.getValue(); bitIndex <= PieceIndex.BlackPawnH.getValue(); ++bitIndex) {
            boolean turnBitOn;
            PromotablePiece promotedToPiece = ((Pawn) _board.getPiece(bitIndex)).getPromotedToPiece();
            if (null == promotedToPiece) {
                turnBitOn = pawnsDesired;
            } else if (promotedToPiece instanceof Queen) {
                turnBitOn = queensDesired;
            } else if (promotedToPiece instanceof Knight){
                turnBitOn = knightsDesired;
            } else if (promotedToPiece instanceof Castle) {
                turnBitOn = castlesDesired;
            } else if (promotedToPiece instanceof Bishop) {
                turnBitOn = bishopsDesired;
            } else {
                throw new InternalError("What was this Pawn promoted to? " + promotedToPiece);
            }

            if (turnBitOn) {
                _bitMask |= (1 << bitIndex);
            } else {
                _bitMask &= ~(1 << bitIndex);
            }
        }
    }


    ChessPieceSet clone(ChessBoard otherBoard) {
        return new ChessPieceSet(this, otherBoard);
    }


    public Set<ChessPiece> getSubSet(PieceFlags pieceFlags) {
        return getSubSet(pieceFlags.getValue());
    }


    public Set<ChessPiece> getSubSet(int pieceFlags) {
        return new ChessPieceSet(_bitMask & pieceFlags, _board);
    }


    public Set<ChessPiece> getAllNotInSet(PieceFlags pieceFlags) {
        return new ChessPieceSet((~_bitMask) & pieceFlags.getValue(), _board);
    }

    @Override
    public synchronized boolean add(ChessPiece piece) {

        if (null == _board) {
            _board = piece.getBoard();
        } else if (piece.getBoard() != _board) {
            throw new IllegalArgumentException("ChessPieceSets can't contain Pieces from different Boards");
        }

        int bit = 1 << piece.getIndex().getValue();

        if ((bit & _bitMask) == 0) {
            _bitMask |= bit;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends ChessPiece> pieces) {
        boolean overallResult = false;

        for(ChessPiece piece : pieces) {
            overallResult = overallResult || add(piece);
        }

        return overallResult;
    }

    @Override
    public synchronized void clear() {
        _bitMask = 0;
        _board = null;
    }

    @Override
    public synchronized boolean contains(Object piece) {
        if (piece instanceof ChessPiece) {
            ChessPiece chessPiece = (ChessPiece) piece;

            if (chessPiece.getBoard() == _board) {
                int bit = 1 << chessPiece.getIndex().getValue();
                return (bit & _bitMask) == bit;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> pieces) {
        for (Object piece : pieces) {
            if (!contains(piece))
                return false;
        }

        return true;
    }

    @Override
    public synchronized boolean equals(Object pieceSet) {
        if (pieceSet instanceof ChessPieceSet) {
            ChessPieceSet pieces = (ChessPieceSet) pieceSet;
            return (_board == pieces._board) && (_bitMask == pieces._bitMask);
        }

        return false;
    }

    @Override
    public synchronized int hashCode() {
        return Objects.hash(_board, _bitMask);
    }

    @Override
    public synchronized boolean isEmpty() { return 0 == _bitMask; }

    @Override
    public Iterator<ChessPiece> iterator() {
        return new Iterator<ChessPiece>() {

            private int _iteratorBitMask = _bitMask;
            private int _bitIndex = 0;

            @Override
            public boolean hasNext() {
                return 0 != _iteratorBitMask;
            }

            @Override
            public ChessPiece next() {
                if (!hasNext())
                    return null;

                while (_bitIndex < Long.SIZE) {
                    int bit = 1 << _bitIndex;

                    if ((bit & _iteratorBitMask) == bit) {
                        _iteratorBitMask &= ~bit; // Turn this bit off
                        ChessPiece piece = ((ChessBoardImpl) _board).getPiece(_bitIndex++);
                        if (piece instanceof Pawn) {
                            Pawn pawn = (Pawn) piece;
                            if (null != pawn.getPromotedToPiece()) {
                                if (null != pawn.getPromotedToPiece().getSquare()) {
                                    // The promotedToPiece is on the board, so return it ...
                                    piece = pawn.getPromotedToPiece();
                                }
                            }
                        }

                        return piece;
                    } else {
                        ++_bitIndex;
                    }
                }

                throw new RuntimeException("ChessPieceSet Iterator algorithm failure!");
            }
        };
    }

    @Override
    public synchronized boolean remove(Object piece) {
        if (piece instanceof ChessPiece) {
            ChessPiece chessPiece = (ChessPiece) piece;

            if (chessPiece.getBoard() == _board) {
                int bit = 1 << chessPiece.getIndex().getValue();

                if ((_bitMask & bit) == bit) {
                    _bitMask &= ~bit; // Turn this bit off

                    if (0 == _bitMask) {
                        // This empty Set longer pertains to any particular ChessBoard ...
                        _board = null;
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> pieces) {
        boolean overallResult = false;

        for (Object piece : pieces) {
            overallResult = overallResult || remove(piece);
        }

        return overallResult;
    }


    @Override
    public boolean retainAll(Collection<?> pieces) {
        long retainBitMask = 0L;

        for (Object piece : pieces) {
            if (piece instanceof ChessPiece) {
                ChessPiece chessPiece = (ChessPiece) piece;

                if (chessPiece.getBoard() == _board) {
                    int bit = 1 << chessPiece.getIndex().getValue();
                    retainBitMask |= bit;
                }
            }
        }

        if ((_bitMask & retainBitMask) != _bitMask) {
            _bitMask &= retainBitMask;
            return true;
        }

        return false;
    }

    @Override
    public synchronized int size() { return Integer.bitCount(_bitMask); }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];

        int i = 0;

        for (ChessPiece e : this) {
            array[i++] = e;
        }

        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        int size = size();
        if (array.length < size) {
            // If array is too small, allocate the new one with the same component type
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
        } else if (array.length > size) {
            // If array is too large, set the first unassigned element to null
            array[size] = null;
        }

        int i = 0;
        for (ChessPiece e : this) {
            // No need for checked cast - ArrayStoreException will be thrown
            // if types are incompatible, just as required
            array[i] = (T) e;
            i++;
        }

        return array;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for(ChessPiece piece : this) {
            String notation = piece.notation();

            switch (piece.getSideColor()) {
                case White:
                    notation = notation.toUpperCase();
                    break;
                case Black:
                    notation = notation.toLowerCase();
                    break;
                default:
                    throw new IllegalArgumentException(
                            String.format("Invalid piece sideColor: %s", piece.getSideColor().toString())
                    );
            }

            sb.append(notation);
        }

        return sb.toString();
    }
}
