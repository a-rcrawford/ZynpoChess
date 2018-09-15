package com.zynpo;

import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.Castle;
import com.zynpo.interfaces.pieces.ChessPiece;
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

        assertCoveredSquares(whitePawn, "a3", "c3");
        assertMightMoveToSquares(whitePawn, "b3", "b4");
        assertMightMoveToNextSquares(whitePawn, "a3", "b3", "c3", "b4");
        assertEquals(0, whitePawn.getMovedCount());
    }

    @Test
    public void pawnMightMoveToBlockedWithEnPassant() {
        ChessBoard board = ChessFactory.createBoard();

        Pawn whitePawn = (Pawn) board.getSquare("d2").getPiece();
        assertEquals(0, whitePawn.getMovedCount());
        whitePawn.setSquare(whitePawn.jumpTwoSquare());
        assertEquals(board.getSquare("d4"), whitePawn.getSquare());
        assertEquals(board.getSquare("d3"), board.getEnPassantSquare());
        assertEquals(1, whitePawn.getMovedCount());

        Pawn blackPawn = (Pawn) board.getSquare("d7").getPiece();
        assertEquals(0, blackPawn.getMovedCount());
        blackPawn.setSquare(blackPawn.squareJustInFront());
        assertEquals(board.getSquare("d6"), blackPawn.getSquare());
        assertNull(board.getEnPassantSquare());
        assertEquals(1, blackPawn.getMovedCount());

        whitePawn.setSquare(whitePawn.squareJustInFront());
        assertEquals(board.getSquare("d5"), whitePawn.getSquare());
        assertNull(board.getEnPassantSquare());
        assertEquals(2, whitePawn.getMovedCount());

        blackPawn = (Pawn) board.getSquare("c7").getPiece();
        blackPawn.setSquare(blackPawn.jumpTwoSquare());
        assertEquals(board.getSquare("c5"), blackPawn.getSquare());
        assertEquals(board.getSquare("c6"), board.getEnPassantSquare());
        assertEquals(1, blackPawn.getMovedCount());

        assertCoveredSquares(whitePawn, "c6", "e6");
        assertMightMoveToSquares(whitePawn,"c6"); // dxc5ep
        assertMightMoveToNextSquares(whitePawn,"c6", "d6", "e6");
    }


    @Test
    public void castleMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Pawn whitePawn = (Pawn) board.getSquare("h2").getPiece();
        assertEquals(0, whitePawn.getMovedCount());
        assertNull(board.getEnPassantSquare());
        whitePawn.setSquare(whitePawn.jumpTwoSquare());
        assertEquals(board.getSquare("h4"), whitePawn.getSquare());
        assertEquals(board.getSquare("h3"), board.getEnPassantSquare());
        assertEquals(1, whitePawn.getMovedCount());

        Pawn blackPawn = (Pawn) board.getSquare("d7").getPiece();
        blackPawn.setSquare(blackPawn.jumpTwoSquare());
        assertEquals(board.getSquare("d5"), blackPawn.getSquare());
        assertEquals(board.getSquare("d6"), board.getEnPassantSquare());
        assertEquals(1, blackPawn.getMovedCount());

        Castle whiteCastle = (Castle) board.getSquare("h1").getPiece();
        assertEquals(0, whiteCastle.getMovedCount());
        whiteCastle.setSquare(board.getSquare("h3"));
        assertEquals(1, whiteCastle.getMovedCount());
        assertNull(board.getEnPassantSquare());

        blackPawn.setSquare(blackPawn.squareJustInFront());
        assertEquals(board.getSquare("d4"), blackPawn.getSquare());
        assertEquals(2, blackPawn.getMovedCount());
        assertNull(board.getEnPassantSquare());

        whiteCastle.setSquare(board.getSquare("b3"));
        assertEquals(2, whiteCastle.getMovedCount());
        assertNull(board.getEnPassantSquare());

        // TODO: Pick up from here ...
        blackPawn.setSquare(blackPawn.squareJustInFront());
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

        Knight blackNight = (Knight) board.getSquare("b8").getPiece();
        blackNight.setSquare(board.getSquare("a6"));

        whiteKnight.setSquare(board.getSquare("b5"));

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

    private Set<ChessSquare> getAllBoardSquares(ChessBoard board) {
        return board.getSquares(
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8");
    }

    private void assertCoveredSquares(ChessPiece piece, String... squares) {
        final Set<ChessSquare> expectedCoveredSquares = piece.getBoard().getSquares(squares);
        final Set<ChessSquare> expectedNotToBeCoveredSquares = getAllBoardSquares(piece.getBoard());
        expectedNotToBeCoveredSquares.removeAll(expectedCoveredSquares);

        for (ChessSquare expectedCoveredSquare : expectedCoveredSquares) {
            assertTrue(String.format("%s should cover %s", piece.toString(), expectedCoveredSquare.toString()),
                    piece.covers(expectedCoveredSquare));
        }

        for (ChessSquare notToBeCoveredSquare : expectedNotToBeCoveredSquares) {
            assertFalse(String.format("%s should not cover %s", piece.toString(), notToBeCoveredSquare.toString()),
                    piece.covers(notToBeCoveredSquare));
        }
    }

    private void assertMightMoveToSquares(ChessPiece piece, String... squares) {
        final Set<ChessSquare> expectedMightMoveToSquares = piece.getBoard().getSquares(squares);
        final Set<ChessSquare> expectedCantMoveToSquares = getAllBoardSquares(piece.getBoard());
        expectedCantMoveToSquares.removeAll(expectedMightMoveToSquares);

        for (ChessSquare expectedMightMoveToSquare : expectedMightMoveToSquares) {
            assertTrue(String.format("%s might move to %s", piece.toString(), expectedMightMoveToSquare.toString()),
                    piece.mightMoveTo(expectedMightMoveToSquare));
        }

        for (ChessSquare cantMoveToSquares : expectedCantMoveToSquares) {
            assertFalse(String.format("%s can't move to %s", piece.toString(), cantMoveToSquares.toString()),
                    piece.mightMoveTo(cantMoveToSquares));
        }

        assertEquals(expectedMightMoveToSquares, piece.potentialMoveSquares(PotentialMoveReason.ForNextMove));
    }

    private void assertMightMoveToNextSquares(ChessPiece piece, String... squares) {
        final Set<ChessSquare> expectedMightMoveToNextSquares = piece.getBoard().getSquares(squares);
        assertEquals(expectedMightMoveToNextSquares, piece.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));
    }
}
