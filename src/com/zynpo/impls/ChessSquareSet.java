package com.zynpo.impls;

import java.lang.reflect.Array;
import java.util.*;

import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;


public class ChessSquareSet implements Set<ChessSquare> {

    long _bitMask;
    ChessBoard _board;


    ChessSquareSet() {
        _bitMask = 0L;
        _board = null;
    }

    @Override
    public boolean add(ChessSquare square) {

        if (null == _board) {
            _board = square.getBoard();
        } else if (square.getBoard() != _board) {
            throw new IllegalArgumentException("ChessSquareSets can't contain Squares from different Boards");
        }

        long bit = 1L << square.getIndex();

        if ((bit & _bitMask) == 0L) {
            _bitMask |= bit;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends ChessSquare> squares) {
        boolean overallResult = false;

        for(ChessSquare square : squares) {
            overallResult = overallResult || add(square);
        }

        return overallResult;
    }

    @Override
    public void clear() { _bitMask = 0L; }

    @Override
    public boolean contains(Object square) {
        if (square instanceof ChessSquare) {
            ChessSquare chessSquare = (ChessSquare) square;

            if (chessSquare.getBoard() == _board) {
                long bit = 1L << chessSquare.getIndex();
                return (bit & _bitMask) == bit;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> squares) {
        for (Object square : squares) {
            if (!contains(square))
                return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object squareSet) {
        if (squareSet instanceof ChessSquareSet) {
            ChessSquareSet squares = (ChessSquareSet) squareSet;
            return (_board == squares._board) && (_bitMask == squares._bitMask);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_board, _bitMask);
    }

    @Override
    public boolean isEmpty() { return 0L == _bitMask; }

    @Override
    public Iterator<ChessSquare> iterator() {
        return new Iterator<ChessSquare>() {

            private long _iteratorBitMask = _bitMask;
            private int _bitIndex = 0;

            @Override
            public boolean hasNext() {
                return 0L != _iteratorBitMask;
            }

            @Override
            public ChessSquare next() {
                if (!hasNext())
                    return null;

                while (_bitIndex < Long.SIZE) {
                    long bit = 1L << _bitIndex;

                    if ((bit & _iteratorBitMask) == bit) {
                        _iteratorBitMask &= ~bit; // Turn this bit off
                        return _board.getSquare(_bitIndex++);
                    } else {
                        ++_bitIndex;
                    }
                }

                throw new RuntimeException("ChessSquareSet Iterator algorithm failure!");
            }
        };
    }

    @Override
    public boolean remove(Object square) {
        if (square instanceof ChessSquare) {
            ChessSquare chessSquare = (ChessSquare) square;

            if (chessSquare.getBoard() == _board) {
                long bit = 1L << chessSquare.getIndex();

                if ((_bitMask & bit) == bit) {
                    _bitMask &= ~bit; // Turn this bit off
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> squares) {
        boolean overallResult = false;

        for (Object square : squares) {
            overallResult = overallResult || remove(square);
        }

        return overallResult;
    }


    @Override
    public boolean retainAll(Collection<?> squares) {
        long retainBitMask = 0L;

        for (Object square : squares) {
            if (square instanceof ChessSquare) {
                ChessSquare chessSquare = (ChessSquare) square;

                if (chessSquare.getBoard() == _board) {
                    long bit = 1L << chessSquare.getIndex();
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
    public int size() { return Long.bitCount(_bitMask); }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];

        for(int i = 0; i < array.length; ++i) {
            array[i] = _board.getSquare(i);
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
            // If array is to large, set the first unassigned element to null
            array[size] = null;
        }

        int i = 0;
        for (ChessSquare e: this) {
            // No need for checked cast - ArrayStoreException will be thrown
            // if types are incompatible, just as required
            array[i] = (T) e;
            i++;
        }

        return array;
    }
}