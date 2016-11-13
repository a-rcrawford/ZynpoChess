package com.zynpo;

import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import org.junit.Test;

import static org.junit.Assert.*;


public class ChessBoardTest {
    
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

}