package com.zynpo;

import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.*;
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


    @Test
    public void equalityTestInitialBoard() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        assertFalse(board == board2);
        assertEquals(board, board2);
        assertTrue(boardsEqualWithSamePieceMovedCounts(board, board2));

        board2 = board;
        assertTrue(board == board2);
        assertEquals(board, board2);
        assertTrue(boardsEqualWithSamePieceMovedCounts(board, board2));

        board2 = board.clone();
        assertFalse(board == board2);
        assertEquals(board, board2);
        assertEquals(board2, board);
        assertTrue(boardsEqualWithSamePieceMovedCounts(board, board2));
        assertTrue(boardsEqualWithSamePieceMovedCounts(board2, board));
    }


    @Test
    public void inequalityTestInitialBoard() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        Knight whiteKnight = (Knight) board.getSquare("b1").getPiece();
        Knight blackKnight = (Knight) board.getSquare("b8").getPiece();

        whiteKnight.moveToSquare(board.getSquare("a3"));
        blackKnight.moveToSquare(board.getSquare("a6"));

        whiteKnight.moveToSquare(board.getSquare("b1"));
        blackKnight.moveToSquare(board.getSquare("b8"));

        assertEquals(board, board2);
        assertFalse(boardsEqualWithSamePieceMovedCounts(board, board2));
    }


    @Test
    public void equalityTestSimplePawns() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        assertTrue(boardsEqualWithSamePieceMovedCounts(board, board2));

        Pawn whitePawn = (Pawn) board.getSquare("a2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        assertNotEquals(board, board2);

        whitePawn = (Pawn) board2.getSquare("a2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        assertTrue(boardsEqualWithSamePieceMovedCounts(board, board2));

        Pawn blackPawn = (Pawn) board.getSquare("a7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());

        assertNotEquals(board, board2);

        blackPawn = (Pawn) board2.getSquare("b7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());

        whitePawn = (Pawn) board.getSquare("b2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        whitePawn = (Pawn) board2.getSquare("b2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        blackPawn = (Pawn) board.getSquare("b7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());

        blackPawn = (Pawn) board2.getSquare("a7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());

        assertEquals(board, board2);
        assertTrue(boardsEqualWithSamePieceMovedCounts(board, board2));

        board2 = board.clone();
        assertFalse(board == board2);
        assertEquals(board, board2);
    }

    @Test
    public void equalityTestEnPassant() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        //-----------------------------------------------
        // Position the first board for en-passant ...
        //-----------------------------------------------

        Pawn whitePawn = (Pawn) board.getSquare("a2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        Knight blackNight = (Knight) board.getSquare("g8").getPiece();
        blackNight.moveToSquare(board.getSquare("h6"));

        whitePawn.moveToSquare(whitePawn.squareJustInFront());

        Pawn blackPawn = (Pawn) board.getSquare("b7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());

        //-----------------------------------------------
        // Position the second board just like it,
        // but without en-passant ...
        //-----------------------------------------------

        Pawn whitePawn2 = (Pawn) board2.getSquare("a2").getPiece();
        whitePawn2.moveToSquare(whitePawn2.squareJustInFront());

        Knight blackNight2 = (Knight) board2.getSquare("g8").getPiece();
        blackNight2.moveToSquare(board2.getSquare("h6"));

        whitePawn2.moveToSquare(whitePawn2.squareJustInFront());

        Pawn blackPawn2 = (Pawn) board2.getSquare("b7").getPiece();
        blackPawn2.moveToSquare(blackPawn2.squareJustInFront());
        whitePawn2.moveToSquare(whitePawn2.squareJustInFront());
        blackPawn2.moveToSquare(blackPawn2.squareJustInFront());

        // FALSE STATEMENT: Should not be equal because the first board can en-passant, and the second can't ...
        //assertNotEquals(board, board2);
        // TRUE STATEMENT: Consider two boards equal if they have the same pieces in the same places ...
        assertEquals(board, board2);

        blackNight.moveToSquare(board.getSquare("g8"));
        assertNotEquals(board, board2);
        blackNight2.moveToSquare(board2.getSquare("g8"));
        // Should now be equal ...
        assertEquals(board, board2);

        board2 = board.clone();
        assertFalse(board == board2);
        assertEquals(board, board2);
    }


    @Test
    public void equalityTestCanCastle() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        //--------------------------------------------------------
        // Position the first board for a king's castle right ...
        //--------------------------------------------------------

        Pawn whitePawn = (Pawn) board.getSquare("e2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        Knight blackNight = (Knight) board.getSquare("g8").getPiece();
        blackNight.moveToSquare(board.getSquare("h6"));

        Bishop whiteBishop = (Bishop) board.getSquare("f1").getPiece();
        whiteBishop.moveToSquare(board.getSquare("e2"));

        blackNight.moveToSquare(board.getSquare("g8"));

        Knight whiteKnight = (Knight) board.getSquare("g1").getPiece();
        whiteKnight.moveToSquare(board.getSquare("f3"));

        blackNight.moveToSquare(board.getSquare("h6"));

        King whiteKing = (King) board.getSquare("e1").getPiece();
        assertTrue(whiteKing.mightMoveTo(board.getSquare("g1")));

        //--------------------------------------------------------
        // Position the second board for a king's castle right ...
        //--------------------------------------------------------

        Pawn whitePawn2 = (Pawn) board2.getSquare("e2").getPiece();
        whitePawn2.moveToSquare(whitePawn2.jumpTwoSquare());

        Knight blackNight2 = (Knight) board2.getSquare("g8").getPiece();
        blackNight2.moveToSquare(board2.getSquare("h6"));

        Bishop whiteBishop2 = (Bishop) board2.getSquare("f1").getPiece();
        whiteBishop2.moveToSquare(board2.getSquare("e2"));

        blackNight2.moveToSquare(board2.getSquare("g8"));

        Knight whiteKnight2 = (Knight) board2.getSquare("g1").getPiece();
        whiteKnight2.moveToSquare(board2.getSquare("f3"));

        blackNight2.moveToSquare(board2.getSquare("h6"));

        King whiteKing2 = (King) board2.getSquare("e1").getPiece();
        assertTrue(whiteKing2.mightMoveTo(board2.getSquare("g1")));

        // At this point both boards should be equal ...
        assertEquals(board, board2);

        // Make the first board different, because the white king can no longer castle ...
        Castle whiteCastle = (Castle) board.getSquare("h1").getPiece();
        whiteCastle.moveToSquare(board.getSquare("f1"));
        blackNight.moveToSquare(board.getSquare("g8"));
        whiteCastle.moveToSquare(whiteCastle.getOrigSquare());
        blackNight.moveToSquare(board.getSquare("h6"));

        // FALSE STATEMENT: Now the boards are different because the king can only castle on the second board ...
        //assertNotEquals(board, board2);
        // TRUE STATEMENT: Boards are considered equal if they have the same pieces in the same position ...
        assertEquals(board, board2);

        // Make the second board the same, because its white king can no longer castle ...
        Castle whiteCastle2 = (Castle) board2.getSquare("h1").getPiece();
        whiteCastle2.moveToSquare(board2.getSquare("g1"));
        blackNight2.moveToSquare(board2.getSquare("g8"));
        whiteCastle2.moveToSquare(whiteCastle2.getOrigSquare());
        blackNight2.moveToSquare(board2.getSquare("h6"));

        // Now the boards should be the same again ...
        assertEquals(board, board2);

        board2 = board.clone();
        assertFalse(board == board2);
        assertEquals(board, board2);
    }


    /**
     * Shouldn't even be calling this routine if board != board2
     * @param board
     * @param board2
     * @return true if the boards equal, and all pieces have the same getMovedCount()
     */
    public static boolean boardsEqualWithSamePieceMovedCounts(ChessBoard board, ChessBoard board2) {
        assertEquals(board.getRowCount(), board2.getRowCount());
        assertEquals(board.getColCount(), board2.getColCount());
        assertEquals(board, board2);

        for (int row = 0; row < board.getRowCount(); ++row) {
            for (int col = 0; col < board.getColCount(); ++col) {
                ChessPiece piece = board.getSquare(row, col).getPiece();
                ChessPiece piece2 = board2.getSquare(row, col).getPiece();
                assertEquals(piece, piece2);

                if (null != piece) {
                    if (piece.getMovedCount() != piece2.getMovedCount()) {
                        System.out.println("" + piece + " move count differs: "
                                + piece.getMovedCount() + " vs. " + piece2.getMovedCount());
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
