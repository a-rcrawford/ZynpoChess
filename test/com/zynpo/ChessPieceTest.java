package com.zynpo;

import com.zynpo.enums.PotentialMoveReason;
import com.zynpo.impls.ChessFactory;
import com.zynpo.interfaces.ChessBoard;
import com.zynpo.interfaces.ChessSquare;
import com.zynpo.interfaces.pieces.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ChessPieceTest extends Assert {

    @Test
    public void pieceSetCanContainMultipleSamePieces() {
        ChessBoard board = ChessFactory.createBoard();

        Set<ChessPiece> pieces = ChessFactory.createChessPieceSet();
        pieces.add(board.getSquare("a2").getPiece());
        pieces.add(board.getSquare("b2").getPiece());
        pieces.add(board.getSquare("c2").getPiece());
        pieces.add(board.getSquare("a7").getPiece());

        assertEquals(4, pieces.size());

        Object[] piecesAsObjects = pieces.toArray();
        ChessPiece[] piecesAsArray = pieces.toArray(new ChessPiece[5]);

        Pawn pawn = (Pawn) board.getSquare("a2").getPiece();
        assertTrue(pieces.contains(pawn));
        assertEquals(piecesAsObjects[0], pawn);
        assertEquals(piecesAsArray[0], pawn);

        pawn = (Pawn) board.getSquare("b2").getPiece();
        assertTrue(pieces.contains(pawn));
        assertEquals(piecesAsObjects[1], pawn);
        assertEquals(piecesAsArray[1], pawn);

        pawn = (Pawn) board.getSquare("c2").getPiece();
        assertTrue(pieces.contains(pawn));
        assertEquals(piecesAsObjects[2], pawn);
        assertEquals(piecesAsArray[2], pawn);

        pawn = (Pawn) board.getSquare("a7").getPiece();
        assertTrue(pieces.contains(pawn));
        assertEquals(piecesAsObjects[3], pawn);
        assertEquals(piecesAsArray[3], pawn);

        pawn = (Pawn) board.getSquare("b7").getPiece();
        assertFalse(pieces.contains(pawn));
        assertEquals(4, piecesAsObjects.length);
        assertNull(piecesAsArray[4]);

        Castle castle = (Castle) board.getSquare("a1").getPiece();
        assertFalse(pieces.contains(castle));
    }

    @Test(expected = InternalError.class)
    public void cantMovePieceFromOneBoardToAnother() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        Pawn pawn = (Pawn) board.getSquare("a2").getPiece();
        pawn.moveToSquare(board2.getSquare("a4"));
    }

    @Test
    public void equalityTest() {
        ChessBoard board = ChessFactory.createBoard();
        ChessBoard board2 = ChessFactory.createBoard();

        Castle castle = (Castle) board.getSquare("a1").getPiece();
        Castle castle2 = (Castle) board2.getSquare("a1").getPiece();

        assertEquals(castle, castle);
        assertEquals(castle, castle2);

        castle2 = (Castle) board2.getSquare("h1").getPiece();
        assertEquals(castle, castle2);

        castle2 = (Castle) board2.getSquare("a8").getPiece();
        assertNotEquals(castle, castle2); // Not the same side color.

        Knight knight = (Knight) board.getSquare("b1").getPiece();
        Knight knight2 = (Knight) board2.getSquare("b1").getPiece();

        assertEquals(knight, knight2);
        assertNotEquals(knight, castle);
        assertNotEquals(knight, castle2);

        for (char col = 'a'; col <= 'h'; ++col) {
            for (char row = '1'; row <= '8'; ++row) {
                String notation = "" + col + row;
                assertEquals(
                        board.getSquare(notation).getPiece(),
                        board2.getSquare(notation).getPiece());
            }
        }

        // Pieces are still counted as equal if they haven't moved the same number of times ...
        knight2.moveToSquare(board2.getSquare("c3"));
        assertEquals(knight, knight2);
        knight.moveToSquare(board.getSquare("a3"));
        assertEquals(knight, knight2);
    }

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
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());
        assertEquals(board.getSquare("d4"), whitePawn.getSquare());
        // Not considered the en-passant square if no pawn can ever move to it ...
        assertNull(board.getEnPassantSquare());
        assertEquals(1, whitePawn.getMovedCount());

        Pawn blackPawn = (Pawn) board.getSquare("d7").getPiece();
        assertEquals(0, blackPawn.getMovedCount());
        blackPawn.moveToSquare(blackPawn.squareJustInFront());
        assertEquals(board.getSquare("d6"), blackPawn.getSquare());
        assertNull(board.getEnPassantSquare());
        assertEquals(1, blackPawn.getMovedCount());

        whitePawn.moveToSquare(whitePawn.squareJustInFront());
        assertEquals(board.getSquare("d5"), whitePawn.getSquare());
        assertNull(board.getEnPassantSquare());
        assertEquals(2, whitePawn.getMovedCount());

        blackPawn = (Pawn) board.getSquare("c7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());
        assertEquals(board.getSquare("c5"), blackPawn.getSquare());
        // The white pawn on d5 can take the black pawn on c5 via en-passant ...
        assertEquals(board.getSquare("c6"), board.getEnPassantSquare());
        assertEquals(1, blackPawn.getMovedCount());

        assertCoveredSquares(whitePawn, "c6", "e6");
        assertMightMoveToSquares(whitePawn,"c6"); // dxc5ep
        assertMightMoveToNextSquares(whitePawn,"c6", "d6", "e6");

        blackPawn.takeBackToSquare(board.getSquare("c7"), null);
        assertEquals(0, blackPawn.getMovedCount());

        whitePawn.takeBackToSquare(board.getSquare("d4"), null);
        assertEquals(1, whitePawn.getMovedCount());
    }


    @Test
    public void castleMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Pawn whitePawn = (Pawn) board.getSquare("h2").getPiece();
        assertEquals(0, whitePawn.getMovedCount());
        assertNull(board.getEnPassantSquare());
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());
        assertEquals(board.getSquare("h4"), whitePawn.getSquare());
        // No pawn is able to take the pawn on h4 via en-passant ...
        assertNull(board.getEnPassantSquare());
        assertEquals(1, whitePawn.getMovedCount());

        Pawn blackPawn = (Pawn) board.getSquare("d7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());
        assertEquals(board.getSquare("d5"), blackPawn.getSquare());
        assertEquals(1, blackPawn.getMovedCount());

        Castle whiteCastle = (Castle) board.getSquare("h1").getPiece();
        assertEquals(0, whiteCastle.getMovedCount());
        whiteCastle.moveToSquare(board.getSquare("h3"));
        assertEquals(1, whiteCastle.getMovedCount());
        assertNull(board.getEnPassantSquare());

        blackPawn.moveToSquare(blackPawn.squareJustInFront());
        assertEquals(board.getSquare("d4"), blackPawn.getSquare());
        assertEquals(2, blackPawn.getMovedCount());
        assertNull(board.getEnPassantSquare());

        whiteCastle.moveToSquare(board.getSquare("b3"));
        assertEquals(2, whiteCastle.getMovedCount());
        assertNull(board.getEnPassantSquare());

        blackPawn.moveToSquare(blackPawn.squareJustInFront());
        assertEquals(board.getSquare("d3"), blackPawn.getSquare());
        assertCoveredSquares(whiteCastle, "a3", "b2", "c3", "d3", "b4", "b5", "b6", "b7");
        assertMightMoveToSquares(whiteCastle, "a3", "c3", "d3", "b4", "b5", "b6", "b7");
        assertMightMoveToNextSquares(whiteCastle,
                "a3", "b2", "b1", "c3", "d3", "e3", "f3", "g3", "h3", "b4", "b5", "b6", "b7", "b8");
    }


    @Test
    public void knightMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Knight whiteKnight = (Knight) board.getSquare("b1").getPiece();
        assertCoveredSquares(whiteKnight, "a3", "c3", "d2");
        assertMightMoveToSquares(whiteKnight, "a3", "c3");
        assertMightMoveToNextSquares(whiteKnight, "a3", "c3", "d2");

        whiteKnight.moveToSquare(board.getSquare("c3"));

        Knight blackKnight = (Knight) board.getSquare("b8").getPiece();
        blackKnight.moveToSquare(board.getSquare("a6"));

        whiteKnight.moveToSquare(board.getSquare("b5"));

        assertCoveredSquares(blackKnight, "b8", "c7", "c5", "b4");
        assertMightMoveToSquares(blackKnight, "b8", "c5", "b4");
        assertMightMoveToNextSquares(blackKnight, "b8", "c7", "c5", "b4");

        blackKnight = (Knight) board.getSquare("g8").getPiece();
        blackKnight.moveToSquare(board.getSquare("f6"));

        assertCoveredSquares(whiteKnight, "a7", "c7", "d6", "d4", "c3", "a3");
        assertMightMoveToSquares(whiteKnight, "a7", "c7", "d6", "d4", "c3", "a3");
        assertMightMoveToNextSquares(whiteKnight, "a7", "c7", "d6", "d4", "c3", "a3");

        whiteKnight.moveToSquare(board.getSquare("a6"));

        assertCoveredSquares(blackKnight, "e8", "g8", "h7", "h5", "g4", "e4", "d5", "d7");
        assertMightMoveToSquares(blackKnight, "g8", "h5", "g4", "e4", "d5");
        // The black knight should know that it can't possibly move onto its own king after white moves ...
        assertMightMoveToNextSquares(blackKnight, "g8", "h7", "h5", "g4", "e4", "d5", "d7");
    }


    @Test
    public void bishopMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Bishop whiteBishop = (Bishop) board.getSquare("c1").getPiece();

        assertCoveredSquares(whiteBishop, "b2", "d2");
        assertMightMoveToSquares(whiteBishop); // Can't move anywhere yet.
        assertMightMoveToNextSquares(whiteBishop, "b2", "a3", "d2", "e3", "f4", "g5", "h6");

        Pawn whitePawn = (Pawn) board.getSquare("d2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        Pawn blackPawn = (Pawn) board.getSquare("g7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());

        whiteBishop.moveToSquare(board.getSquare("d2"));
        assertEquals(1, whiteBishop.getMovedCount());

        blackPawn = (Pawn) board.getSquare("a7").getPiece();
        blackPawn.moveToSquare(board.getSquare("a5"));

        assertCoveredSquares(whiteBishop, "c1", "c3", "b4", "a5", "e1", "e3", "f4", "g5");
        assertMightMoveToSquares(whiteBishop, "c1", "c3", "b4", "a5", "e3", "f4", "g5");
        // The bishop should know that it can't possibly move onto its own king ...
        assertMightMoveToNextSquares(whiteBishop, "c1", "c3", "b4", "a5", "e3", "f4", "g5", "h6");

        Bishop blackBishop = (Bishop) board.getSquare("f8").getPiece();
        blackBishop.moveToSquare(board.getSquare("g7"));

        whiteBishop.moveToSquare(board.getSquare("g5"));

        assertCoveredSquares(blackBishop, "f8", "h8", "h6", "f6", "e5", "d4");
        assertMightMoveToSquares(blackBishop, "f8", "h6", "f6", "e5", "d4");
        assertMightMoveToNextSquares(blackBishop, "f8", "h8", "h6", "f6", "e5", "d4", "c3", "b2", "a1");
    }

    @Test
    public void queenMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        Queen whiteQueen = (Queen) board.getSquare("d1").getPiece();

        assertCoveredSquares(whiteQueen, "c1", "c2", "d2", "e2", "e1");
        assertMightMoveToSquares(whiteQueen); // The queen shouldn't be able to move anywhere at first
        assertMightMoveToNextSquares(whiteQueen,
                "c1", "b1", "a1", "c2", "b3", "a4",
                "d2", "d3", "d4", "d5", "d6", "d7", "d8",
                "e2", "f3", "g4", "h5");

        Pawn whitePawn = (Pawn) board.getSquare("d2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());

        Pawn blackPawn = (Pawn) board.getSquare("e7").getPiece();
        ChessPiece takenPiece = blackPawn.moveToSquare(blackPawn.jumpTwoSquare());
        assertNull(takenPiece);

        takenPiece = whitePawn.moveToSquare(board.getSquare("e5"));
        assertEquals(blackPawn, takenPiece);
        assertTrue(takenPiece == blackPawn);

        Queen blackQueen = (Queen) board.getSquare("d8").getPiece();

        assertCoveredSquares(blackQueen, "c8", "c7", "d7", "e7", "f6", "g5", "h4", "e8");
        assertMightMoveToSquares(blackQueen, "e7", "f6", "g5", "h4");
        assertMightMoveToNextSquares(blackQueen,
                "c8", "b8", "a8", "c7", "b6", "a5",
                "d7", "d6", "d5", "d4", "d3", "d2", "d1",
                "e7", "f6", "g5", "h4");

        blackPawn = (Pawn) board.getSquare("h7").getPiece();
        blackPawn.moveToSquare(board.getSquare("h5"));

        assertCoveredSquares(whiteQueen, "c1", "c2", "d2", "d3", "d4", "d5", "d6", "d7", "e2", "e1");
        assertMightMoveToSquares(whiteQueen, "d2", "d3", "d4", "d5", "d6", "d7");
        assertMightMoveToNextSquares(whiteQueen,
                "c1", "b1", "a1", "c2", "b3", "a4",
                "d2", "d3", "d4", "d5", "d6", "d7", "d8",
                "e2", "f3", "g4", "h5");
    }


    @Test
    public void kingMightMoveTo() {
        ChessBoard board = ChessFactory.createBoard();

        King whiteKing = (King) board.getSquare("e1").getPiece();

        assertCoveredSquares(whiteKing, "d1", "d2", "e2", "f2", "f1");
        assertMightMoveToSquares(whiteKing); // The king can't move anywhere initially.
        assertMightMoveToNextSquares(whiteKing, "d1", "d2", "e2", "f2", "f1");

        Pawn whitePawn = (Pawn) board.getSquare("e2").getPiece();
        whitePawn.moveToSquare(whitePawn.jumpTwoSquare());
        //System.out.println(board);

        Pawn blackPawn = (Pawn) board.getSquare("f7").getPiece();
        blackPawn.moveToSquare(blackPawn.jumpTwoSquare());
        //System.out.println(board);

        whiteKing.moveToSquare(board.getSquare("e2"));
        //System.out.println(board);

        blackPawn.moveToSquare(blackPawn.squareJustInFront());
        //System.out.println(board);

        assertCoveredSquares(whiteKing, "e1", "d1", "d2", "d3", "e3", "f3", "f2", "f1");
        assertMightMoveToSquares(whiteKing, "e1", "d3", "e3", "f3");
        assertMightMoveToNextSquares(whiteKing, "e1", "d1", "d2", "d3", "e3", "f3", "f2", "f1");

        whiteKing.moveToSquare(board.getSquare("d3"));
        //System.out.println(board);

        blackPawn.moveToSquare(blackPawn.squareJustInFront());
        //System.out.println(board);
        assertEquals(3, blackPawn.getMovedCount());
        assertEquals(board.getSquare("f3"), blackPawn.getSquare());

        whiteKing.moveToSquare(board.getSquare("e3"));
        //System.out.println(board);

        blackPawn = (Pawn) board.getSquare("a7").getPiece();
        blackPawn.moveToSquare(blackPawn.squareJustInFront());
        //System.out.println(board);

        assertCoveredSquares(whiteKing, "e2", "d2", "d3", "d4", "e4", "f4", "f3", "f2");
        assertMightMoveToSquares(whiteKing, "e2", "d3", "d4", "f4", "f3");
        assertMightMoveToNextSquares(whiteKing, "e2", "d2", "d3", "d4", "e4", "f4", "f3", "f2");
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
