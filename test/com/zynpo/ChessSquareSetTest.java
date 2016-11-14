package com.zynpo;

import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.impls.ChessSquareSet;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;


public class ChessSquareSetTest {

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

            assertEquals(3, count);
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


}
