package com.zynpo;

import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import org.junit.Assert;
import org.junit.Test;


public class ChessBoardTest extends Assert {

    @Test
    public void squareExists() throws Exception {
        ChessBoard board = ChessFactory.createBoard();

        assertTrue(board.squareExists(0, 0));
        assertTrue(board.squareExists(0, 7));
        assertTrue(board.squareExists(7, 0));
        assertTrue(board.squareExists(7, 7));

        assertFalse(board.squareExists(-1, 0));
        assertFalse(board.squareExists(0, -1));
        assertFalse(board.squareExists(0, 8));
        assertFalse(board.squareExists(8, 0));
        assertFalse(board.squareExists(8, 8));
    }


    @Test
    public void getSquare() {
        ChessBoard board = ChessFactory.createBoard();

        assertNotNull(board.getSquare(0, 0));
        assertNotNull(board.getSquare(0, 7));
        assertNotNull(board.getSquare(7, 0));
        assertNotNull(board.getSquare(7, 7));

        assertNull(board.getSquare(-1, 0));
        assertNull(board.getSquare(0, -1));
        assertNull(board.getSquare(0, 8));
        assertNull(board.getSquare(8, 0));
        assertNull(board.getSquare(8, 8));

        assertNotNull(board.getSquare("a1"));
        assertNotNull(board.getSquare("H1"));
        assertNotNull(board.getSquare("A8"));
        assertNotNull(board.getSquare("h8"));

        assertNull(board.getSquare("a9"));
        assertNull(board.getSquare("I1"));

        assertEquals(board.getSquare(0, 0), board.getSquare("a1"));
        assertEquals(board.getSquare(0, 7), board.getSquare("h1"));
        assertEquals(board.getSquare(7, 0), board.getSquare("a8"));
        assertEquals(board.getSquare(7, 7), board.getSquare("h8"));
    }


    @Test
    public void iteratorTest() {
        ChessBoard board = ChessFactory.createBoard();
        int squareCount = 0;

        for(ChessSquare square : board) {
            ++squareCount;
        }

        assertEquals(board.getRowCount() * board.getColCount(), squareCount);
    }

}
