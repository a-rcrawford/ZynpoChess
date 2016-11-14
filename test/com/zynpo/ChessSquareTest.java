package com.zynpo;

import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import org.junit.Test;

import static org.junit.Assert.*;


public class ChessSquareTest {

    @Test
    public void blackSquares() {

        String[] blackSquares =
               ("a1 c1 e1 g1 " +
                "b2 d2 f2 h2 " +
                "a3 c3 e3 g3 " +
                "b4 d4 f4 h4 " +
                "a5 c5 e5 g5 " +
                "b6 d6 f6 h6 " +
                "a7 c7 e7 g7 " +
                "b8 d8 f8 h8").split(" ");

        ChessBoard board = ChessFactory.createBoard();

        for (String square : blackSquares) {
            assertEquals(board.getSquare(square).getSideColor(), SideColor.Black);
        }
    }


    @Test
    public void whiteSquares() {

        String[] whiteSquares =
               ("b1 d1 f1 h1 " +
                "a2 c2 e2 g2 " +
                "b3 d3 f3 h3 " +
                "a4 c4 e4 g4 " +
                "b5 d5 f5 h5 " +
                "a6 c6 e6 g6 " +
                "b7 d7 f7 h7 " +
                "a8 c8 e8 g8").split(" ");

        ChessBoard board = ChessFactory.createBoard();

        for (String square : whiteSquares) {
            assertEquals(board.getSquare(square).getSideColor(), SideColor.White);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void rowsAwayFromCountDifferentBoards() throws IllegalArgumentException {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        ChessSquare squareFromBoard1 = board1.getSquare("a1");
        ChessSquare squareFromBoard2 = board2.getSquare("b2");

        squareFromBoard1.rowsAwayFromCount(squareFromBoard2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void colsAwayFromCountDifferentBoards() throws IllegalArgumentException {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        ChessSquare squareFromBoard1 = board1.getSquare("A8");
        ChessSquare squareFromBoard2 = board2.getSquare("h1");

        squareFromBoard1.colsAwayFromCount(squareFromBoard2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void rowsDistanceFromDifferentBoards() throws IllegalArgumentException {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        ChessSquare squareFromBoard1 = board1.getSquare("H1");
        ChessSquare squareFromBoard2 = board2.getSquare("b2");

        squareFromBoard1.rowDistanceFrom(squareFromBoard2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void colDistanceFromDifferentBoards() throws IllegalArgumentException {
        ChessBoard board1 = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        ChessSquare squareFromBoard1 = board1.getSquare("b4");
        ChessSquare squareFromBoard2 = board2.getSquare("g6");

        squareFromBoard1.colDistanceFrom(squareFromBoard2);
    }

}
