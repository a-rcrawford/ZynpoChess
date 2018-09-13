package com.zynpo;

import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
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

        assertTrue(whitePawn.mightMoveTo(board.getSquare("b3")));
        assertTrue(whitePawn.mightMoveTo(board.getSquare("b4")));

        assertFalse(whitePawn.mightMoveTo(board.getSquare("b5")));
        assertFalse(whitePawn.mightMoveTo(board.getSquare("a3")));
        assertFalse(whitePawn.mightMoveTo(board.getSquare("c3")));

        Set<ChessSquare> expectedPotentialMoveSquares = ChessFactory.createChessSquareSet(
                board.getSquare("b3"),
                board.getSquare("b4"));

        // For the next move, we expect this pawn may jump up one or two squares ...
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        // For the move after next, we assume this pawn may be able to take something on its own forward diagonals ...
        expectedPotentialMoveSquares.add(board.getSquare("a3"));
        expectedPotentialMoveSquares.add(board.getSquare("c3"));
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

        Set<ChessSquare> expectedPotentialMoveSquares = ChessFactory.createChessSquareSet(board.getSquare("c6"));

        // For the next move, we only expect this white pawn might move to the en passant square ...
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForNextMove));

        // For the move after next, we assume this pawn may be able to move forward, or take from the other forward diagonal square ...
        expectedPotentialMoveSquares.add(board.getSquare("d6"));
        expectedPotentialMoveSquares.add(board.getSquare("e6"));
        assertEquals(expectedPotentialMoveSquares, whitePawn.potentialMoveSquares(PotentialMoveReason.ForMoveAfterNext));
    }

}
