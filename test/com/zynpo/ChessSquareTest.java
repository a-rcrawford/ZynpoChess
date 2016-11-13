package com.zynpo;

import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import org.junit.Test;

import static org.junit.Assert.*;


public class ChessSquareTest {

    @Test(expected = IllegalArgumentException.class)
    public void rowsAwayFromCount() throws IllegalArgumentException {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        ChessSquare squareFromBoard1 = board1.getSquare("a1");
        ChessSquare squareFromBoard2 = board2.getSquare("b2");

        squareFromBoard1.rowsAwayFromCount(squareFromBoard2);
    }

}
