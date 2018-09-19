package com.zynpo;

import com.zynpo.enums.SideColor;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Knight;
import org.junit.Assert;
import org.junit.Test;


public class ChessSquareTest extends Assert {

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


    @Test
    public void rowsAwayFromCount() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareB3 = board.getSquare("B3");
        ChessSquare squareG5 = board.getSquare("g5");

        assertEquals(-2, squareB3.rowsAwayFromCount(squareG5));
        assertEquals(2, squareG5.rowsAwayFromCount(squareB3));
    }


    @Test
    public void rowsAwayFromCountSameRow() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareD4 = board.getSquare("D4");
        ChessSquare squareH4 = board.getSquare("h4");

        assertEquals(0, squareD4.rowsAwayFromCount(squareH4));
        assertEquals(0, squareH4.rowsAwayFromCount(squareD4));
    }


    @Test
    public void rowsAwayFromCountSameSquare() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare square1 = board.getSquare("F7");
        ChessSquare square2 = board.getSquare("f7");

        assertEquals(square1, square2);
        assertEquals(0, square1.rowsAwayFromCount(square2));
        assertEquals(0, square2.rowsAwayFromCount(square1));
    }


    @Test
    public void colsAwayFromCount() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareA2 = board.getSquare("a2");
        ChessSquare squareF4 = board.getSquare("F4");

        assertEquals(-5, squareA2.colsAwayFromCount(squareF4));
        assertEquals(5, squareF4.colsAwayFromCount(squareA2));
    }


    @Test
    public void colsAwayFromCountSameCol() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareD4 = board.getSquare("d4");
        ChessSquare squareD1 = board.getSquare("D1");

        assertEquals(0, squareD4.colsAwayFromCount(squareD1));
        assertEquals(0, squareD1.colsAwayFromCount(squareD4));
    }


    @Test
    public void colsAwayFromCountSameSquare() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare square1 = board.getSquare("a2");
        ChessSquare square2 = board.getSquare("A2");

        assertEquals(square1, square2);
        assertEquals(0, square1.colsAwayFromCount(square2));
        assertEquals(0, square2.colsAwayFromCount(square1));
    }


    @Test
    public void rowDistance() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareC4 = board.getSquare("C4");
        ChessSquare squareD8 = board.getSquare("d8");

        assertEquals(4, squareC4.rowDistanceFrom(squareD8));
        assertEquals(4, squareD8.rowDistanceFrom(squareC4));
    }


    @Test
    public void colDistance() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareB3 = board.getSquare("b3");
        ChessSquare squareE6 = board.getSquare("E6");

        assertEquals(3, squareB3.colDistanceFrom(squareE6));
        assertEquals(3, squareE6.colDistanceFrom(squareB3));
    }


    @Test
    public void rowDistanceSameSquare() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare square1 = board.getSquare("c6");
        ChessSquare square2 = board.getSquare("C6");

        assertEquals(square1, square2);
        assertEquals(0, square1.colDistanceFrom(square2));
        assertEquals(0, square2.colDistanceFrom(square1));
    }


    @Test
    public void colDistanceSameSquare() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare square1 = board.getSquare("E2");
        ChessSquare square2 = board.getSquare("e2");

        assertEquals(square1, square2);
        assertEquals(0, square1.rowDistanceFrom(square2));
        assertEquals(0, square2.rowDistanceFrom(square1));
    }


    @Test
    public void getRelativeSquare() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareH6 = board.getSquare("h6");
        ChessSquare squareC3 = board.getSquare("C3");

        ChessSquare square = squareH6.getRelativeSquare(-3, -5);
        assertEquals(squareC3, square);
        square = squareC3.getRelativeSquare(3, 5);
        assertEquals(squareH6, square);
    }

    @Test
    public void getRelativeSquaresOffBoard() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareD4 = board.getSquare("d4");
        ChessSquare square = squareD4.getRelativeSquare(0, -4);
        assertNull(square);
        square = squareD4.getRelativeSquare(0, 5);
        assertNull(square);
        square = squareD4.getRelativeSquare(-4, 0);
        assertNull(square);
        square = squareD4.getRelativeSquare(5, 0);
        assertNull(square);
        square = squareD4.getRelativeSquare(-3, 2);
        assertEquals(board.getSquare("f1"), square);
    }


    @Test
    public void getRelativeSquareSameSquare() {
        ChessBoard board = ChessFactory.createBoard();

        ChessSquare squareE5 = board.getSquare("E5");
        ChessSquare square = squareE5.getRelativeSquare(0, 0);
        assertEquals(squareE5, square);
    }


    @Test
    public void equalityTest() {

        String str = new String("Hello World!");
        String str2 = new String("Hello World!");

        // Not the same reference ...
        assertFalse(str == str2);
        // Are counted as the same value ...
        assertTrue(str.equals(str2));
        // assertEquals() checks that values equal, not references ...
        assertEquals(str, str2);

        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        assertEquals(board.getSquare("a1"), board2.getSquare("a1"));

        for (char col = 'a'; col <= 'h'; ++col) {
            for (char row = '1'; row <= '8'; ++row) {
                String notation = "" + col + row;
                assertEquals(
                        board.getSquare(notation),
                        board2.getSquare(notation));
            }
        }

        Knight knight = (Knight) board.getSquare("b1").getPiece();
        Knight knight2 = (Knight) board2.getSquare("b1").getPiece();

        knight.setSquare(board.getSquare("c3"));

        knight2.setSquare(board2.getSquare("a3"));
        knight2.setSquare(board2.getSquare("b5"));
        knight2.setSquare(board2.getSquare("c3"));

        // Though they have moved a different number of times, the
        // knights on this square are considered equal ...
        assertEquals(board.getSquare("c3"), board2.getSquare("c3"));

        knight.setSquare(board.getSquare("a2"));
        knight.setSquare(board.getSquare("c3"));

        // Now the knights on this square have moved the same number of times ...
        assertEquals(board.getSquare("c3"), board2.getSquare("c3"));
    }

}
