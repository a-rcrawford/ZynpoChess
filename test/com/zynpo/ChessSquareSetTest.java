package com.zynpo;

import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;


public class ChessSquareSetTest extends Assert {

    @Test
    public void addRemoveAFew() {

        ChessBoard board = ChessFactory.createBoard();

        Set<ChessSquare> squareSet = ChessFactory.createChessSquareSet();
        assertTrue(squareSet.isEmpty());
        assertEquals(0, squareSet.size());

        assertTrue(squareSet.add(board.getSquare("a1")));
        assertFalse(squareSet.isEmpty());
        assertEquals(1, squareSet.size());

        assertTrue(squareSet.add(board.getSquare("h8")));
        assertEquals(2, squareSet.size());

        assertTrue(squareSet.add(board.getSquare("d4")));
        assertEquals(3, squareSet.size());

        assertFalse(squareSet.add(board.getSquare("d4")));
        assertEquals(3, squareSet.size());

        assertTrue(squareSet.contains(board.getSquare("a1")));
        assertTrue(squareSet.contains(board.getSquare("h8")));
        assertTrue(squareSet.contains(board.getSquare("d4")));

        assertFalse(squareSet.contains(board.getSquare("c3")));

        {
            int count = 0;
            boolean foundA1 = false;
            boolean foundH8 = false;
            boolean foundD4 = false;

            for (ChessSquare square : squareSet) {
                count++;

                if (square.toString().equals("a1"))
                    foundA1 = true;
                else if (square.toString().equals("h8"))
                    foundH8 = true;
                else if (square.toString().equals("d4"))
                    foundD4 = true;
                else
                    fail(String.format("Unexpected square: %s", square.toString()));
            }

            assertTrue(foundA1);
            assertTrue(foundH8);
            assertTrue(foundD4);
            assertEquals(3, count);
        }

        {
            Object[] squareObjectArray = squareSet.toArray();
            assertEquals(3, squareObjectArray.length);
            assertEquals(board.getSquare("a1"), squareObjectArray[0]);
            assertEquals(board.getSquare("d4"), squareObjectArray[1]);
            assertEquals(board.getSquare("h8"), squareObjectArray[2]);
        }

        {
            ChessSquare[] squareArray = new ChessSquare[3];
            squareSet.toArray(squareArray);
            assertEquals(board.getSquare("a1"), squareArray[0]);
            assertEquals(board.getSquare("d4"), squareArray[1]);
            assertEquals(board.getSquare("h8"), squareArray[2]);
        }

        assertTrue(squareSet.remove(board.getSquare("h8")));
        assertEquals(2, squareSet.size());

        assertFalse(squareSet.remove(board.getSquare("h8")));
        assertEquals(2, squareSet.size());

        assertTrue(squareSet.remove(board.getSquare("a1")));
        assertEquals(1, squareSet.size());
        assertFalse(squareSet.isEmpty());

        assertTrue(squareSet.remove(board.getSquare("d4")));
        assertEquals(0, squareSet.size());
        assertTrue(squareSet.isEmpty());
    }


    @Test
    public void addRemoveEntireBoard() {
        ChessBoard board = ChessFactory.createBoard();
        Set<ChessSquare> chessSquareSet = ChessFactory.createChessSquareSet();

        int expectedCount = 0;
        assertEquals(expectedCount, chessSquareSet.size());

        for (int row = 0; row < board.getColCount(); ++row) {
            for (int col = 0; col < board.getRowCount(); ++col) {
                chessSquareSet.add(board.getSquare(row, col));
                assertEquals(++expectedCount, chessSquareSet.size());
            }
        }

        for (int col = 0; col < board.getColCount(); ++col) {
             for (int row = 0; row < board.getRowCount(); ++row) {
                chessSquareSet.remove(board.getSquare(row, col));
                assertEquals(--expectedCount, chessSquareSet.size());
            }
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void cantHoldSquaresFrom2BoardsAtSameTime() {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        Set<ChessSquare> chessSquareSet = ChessFactory.createChessSquareSet();

        chessSquareSet.add(board1.getSquare("a1"));
        chessSquareSet.add(board2.getSquare("H1"));
    }


    @Test
    public void canHoldSquaresFrom2BoardsAtDifferentTimes() {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        Set<ChessSquare> chessSquareSet = ChessFactory.createChessSquareSet();

        chessSquareSet.add(board1.getSquare("a1"));
        chessSquareSet.clear();
        chessSquareSet.add(board2.getSquare("H1"));
        chessSquareSet.remove(board2.getSquare("h1"));
        chessSquareSet.add(board1.getSquare("c4"));
    }
}
