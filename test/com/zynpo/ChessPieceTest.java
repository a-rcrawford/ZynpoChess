package com.zynpo;

import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Castle;
import com.zynpo.interfaces.pieces.Knight;
import com.zynpo.interfaces.pieces.Pawn;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ChessPieceTest extends Assert {

    @Test
    public void pawnMightMoveToBeginning() {
        ChessBoard board = ChessFactory.createBoard();

        Pawn whitePawn = (Pawn) board.getSquare("b2").getPiece();

        assertEquals(board.getSquare("b3"), whitePawn.squareJustInFront());
        assertEquals(board.getSquare("b4"), whitePawn.jumpTwoSquare());
        assertTrue(whitePawn.covers(board.getSquare("a3")));
        assertFalse(whitePawn.covers(board.getSquare("b3")));
        assertFalse(whitePawn.covers(board.getSquare("b4")));
        assertTrue(whitePawn.covers(board.getSquare("c3")));

        assertTrue(whitePawn.mightMoveTo(board.getSquare("b3")));
        assertTrue(whitePawn.mightMoveTo(board.getSquare("b4")));

        assertFalse(whitePawn.mightMoveTo(board.getSquare("b5")));
        assertFalse(whitePawn.mightMoveTo(board.getSquare("a3")));
        assertFalse(whitePawn.mightMoveTo(board.getSquare("c3")));

        Set<ChessSquare> expectedPotentialMoveSquares = board.getSquares("b3", "b4");

        // For the next move, we expect this pawn may jump up one or two squares ...
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        // For the move after next, we assume this pawn may be able to take something on its own forward diagonals ...
        expectedPotentialMoveSquares.addAll(board.getSquares("a3", "c3"));
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));
    }

    @Test
    public void pawnMightMoveToBlockedWithEnPassant() {
        ChessBoard board = ChessFactory.createBoard();

        Pawn whitePawn = (Pawn) board.getSquare("d2").getPiece();
        whitePawn.setSquare(whitePawn.jumpTwoSquare());
        whitePawn.incrementMovedCount();
        assertEquals(board.getSquare("d4"), whitePawn.getSquare());

        Pawn blackPawn = (Pawn) board.getSquare("d7").getPiece();
        blackPawn.setSquare(blackPawn.squareJustInFront());
        blackPawn.incrementMovedCount();
        assertEquals(board.getSquare("d6"), blackPawn.getSquare());

        whitePawn.setSquare(whitePawn.squareJustInFront());
        whitePawn.incrementMovedCount();
        assertEquals(board.getSquare("d5"), whitePawn.getSquare());

        blackPawn = (Pawn) board.getSquare("c7").getPiece();
        blackPawn.setSquare(blackPawn.jumpTwoSquare());
        blackPawn.incrementMovedCount();
        assertEquals(board.getSquare("c5"), blackPawn.getSquare());
        board.setEnPassantSquare(board.getSquare("c6"));

        assertTrue("White pawn might dxc5ep", whitePawn.mightMoveTo(board.getSquare("c6")));
        assertFalse("White pawn forward blocked", whitePawn.mightMoveTo(whitePawn.squareJustInFront()));
        assertFalse("White pawn can't take anything on e4", whitePawn.mightMoveTo(board.getSquare("e6")));

        assertTrue(whitePawn.covers(board.getSquare("c6")));
        assertFalse(whitePawn.covers(board.getSquare("d6")));
        assertTrue(whitePawn.covers(board.getSquare("e6")));

        Set<ChessSquare> expectedPotentialMoveSquares = board.getSquares("c6");

        // For the next move, we only expect this white pawn might move to the en passant square ...
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        // For the move after next, we assume this pawn may be able to move forward, or take from the other forward diagonal square ...
        expectedPotentialMoveSquares.addAll(board.getSquares("d6", "e6"));
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));
    }


    @Test
    public void castleMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Pawn whitePawn = (Pawn) board.getSquare("h2").getPiece();
        whitePawn.setSquare(whitePawn.jumpTwoSquare());
        whitePawn.incrementMovedCount();

        Pawn blackPawn = (Pawn) board.getSquare("d7").getPiece();
        blackPawn.setSquare(blackPawn.jumpTwoSquare());
        blackPawn.incrementMovedCount();
        assertEquals(board.getSquare("d5"), blackPawn.getSquare());

        Castle whiteCastle = (Castle) board.getSquare("h1").getPiece();
        whiteCastle.setSquare(board.getSquare("h3"));
        whiteCastle.incrementMovedCount();

        blackPawn.setSquare(blackPawn.squareJustInFront());
        blackPawn.incrementMovedCount();
        assertEquals(board.getSquare("d4"), blackPawn.getSquare());

        whiteCastle.setSquare(board.getSquare("b3"));
        whiteCastle.incrementMovedCount();

        blackPawn.setSquare(blackPawn.squareJustInFront());
        blackPawn.incrementMovedCount();
        assertEquals(board.getSquare("d3"), blackPawn.getSquare());

        Set<ChessSquare> expectedPotentialMoveSquares = board.getSquares("a3", "c3", "d3", "b4", "b5", "b6", "b7");
        assertEquals(expectedPotentialMoveSquares, whiteCastle.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        for(ChessSquare square : expectedPotentialMoveSquares) {
            assertTrue(whiteCastle.covers(square));
            assertTrue(whiteCastle.mightMoveTo(square));
        }

        assertTrue(whiteCastle.covers(board.getSquare("b2")));
        assertFalse(whiteCastle.covers(board.getSquare("b1")));
        assertFalse(whiteCastle.covers(board.getSquare("d4")));
        assertFalse(whiteCastle.covers(board.getSquare("b8")));
        assertFalse(whiteCastle.covers(whiteCastle.getSquare()));

        expectedPotentialMoveSquares.addAll(board.getSquares("e3", "f3", "g3", "h3", "b2", "b1", "b8"));
        assertEquals(expectedPotentialMoveSquares, whiteCastle.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));
    }


    @Test
    public void knightMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Knight whiteKnight = (Knight) board.getSquare("b1").getPiece();

        // The Knight covers/protects the pawn on d2, but can't potentially move to take a pawn on its own side ...
        assertTrue(whiteKnight.covers(board.getSquare("d2")));
        assertFalse(whiteKnight.mightMoveTo(board.getSquare("d2")));

        Set<ChessSquare> expectedPotentialMoveSquares = board.getSquares("a3", "c3");
        assertEquals(expectedPotentialMoveSquares, whiteKnight.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        whiteKnight.setSquare(board.getSquare("c3"));
        whiteKnight.incrementMovedCount();

        Knight blackNight = (Knight) board.getSquare("b8").getPiece();
        blackNight.setSquare(board.getSquare("a6"));
        blackNight.incrementMovedCount();

        whiteKnight.setSquare(board.getSquare("b5"));
        whiteKnight.incrementMovedCount();

        expectedPotentialMoveSquares = board.getSquares("b8", "c5", "b4");
        assertEquals(expectedPotentialMoveSquares, blackNight.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        for (ChessSquare square : expectedPotentialMoveSquares) {
            assertTrue(blackNight.mightMoveTo(square));
            assertTrue(blackNight.covers(square));
        }

        // Black knight covers black pawn on c7 ...
        assertTrue(blackNight.covers(board.getSquare("c7")));
        // Black knight can't take black pawn ...
        assertFalse(blackNight.mightMoveTo(board.getSquare("c7")));

        expectedPotentialMoveSquares.add(board.getSquare("c7"));
        assertEquals(expectedPotentialMoveSquares, blackNight.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));

        expectedPotentialMoveSquares = board.getSquares("a7", "c7", "d6", "d4", "c3", "a3");
        assertEquals(expectedPotentialMoveSquares, whiteKnight.potentialMoveSquares(PotentialMoveReason.ForNextMove));
        assertEquals(expectedPotentialMoveSquares, whiteKnight.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));

        for(ChessSquare square : expectedPotentialMoveSquares) {
            assertTrue(whiteKnight.covers(square));
            assertTrue(whiteKnight.mightMoveTo(square));
        }
    }
}
