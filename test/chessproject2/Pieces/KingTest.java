package chessproject2.Pieces;

import chessproject2.Game;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests King.java, focusing on standard moves, king-safety, and castling rules.
 */
public class KingTest {

    private Piece[][] board;

    @Before
    public void setUp() {
        board = new Piece[8][8];
        Game.board = board; // Assign to the static field used by Game.isSquareAttacked
    }

    /**
     * Helper method to check if a move {r, c} exists in the list of valid moves.
     */
    private boolean moveExists(int[][] moves, int r, int c) {
        for (int[] move : moves) {
            if (move[0] == r && move[1] == c) {
                return true;
            }
        }
        return false;
    }
    
    @Test
    public void testKingStandardMoves() {
        King king = new King(true, 3, 3);
        board[3][3] = king;
        int[][] moves = king.ValidMoves(board, false);

        assertEquals("King in center should have 8 moves", 8, moves.length);
        assertTrue("Should be able to move to [2,2]", moveExists(moves, 2, 2));
        assertTrue("Should be able to move to [4,4]", moveExists(moves, 4, 4));
    }

    @Test
    public void testKingCannotMoveIntoCheck() {
        King king = new King(true, 3, 3);
        board[3][3] = king;
        board[0][4] = new Rook(false, 0, 4); // Black rook attacks column 4

        int[][] moves = king.ValidMoves(board, true); // checkDiscoved = true

        assertFalse("King should not move into check at [2,4]", moveExists(moves, 2, 4));
        assertFalse("King should not move into check at [3,4]", moveExists(moves, 3, 4));
        assertFalse("King should not move into check at [4,4]", moveExists(moves, 4, 4));
        assertEquals("King should only have 5 safe moves", 5, moves.length);
    }
    
    @Test
    public void testKingCannotMoveAdjacentToEnemyKing() {
        King whiteKing = new King(true, 0, 0);
        board[0][0] = whiteKing;
        board[0][2] = new King(false, 0, 2); // Black king

        int[][] moves = whiteKing.ValidMoves(board, false);

        assertFalse("King should not move adjacent to other king at [0,1]", moveExists(moves, 0, 1));
        assertFalse("King should not move adjacent to other king at [1,1]", moveExists(moves, 1, 1));
        assertTrue("King should be able to move to safe square [1,0]", moveExists(moves, 1, 0));
    }

    // --- Castling Tests ---

    @Test
    public void testKingSideCastleLegal() {
        King king = new King(true, 0, 4);
        king.turnMoved = -1; // Has not moved
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 7);
        rook.turnMoved = -1; // Has not moved
        board[0][7] = rook;

        int[][] moves = king.ValidMoves(board, false);
        assertTrue("King-side castle [0,6] should be a valid move", moveExists(moves, 0, 6));
    }

    @Test
    public void testQueenSideCastleLegal() {
        King king = new King(true, 0, 4);
        king.turnMoved = -1; // Has not moved
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 0);
        rook.turnMoved = -1; // Has not moved
        board[0][0] = rook;

        int[][] moves = king.ValidMoves(board, false);
        assertTrue("Queen-side castle [0,2] should be a valid move", moveExists(moves, 0, 2));
    }

    @Test
    public void testCastleBlocked() {
        King king = new King(true, 0, 4);
        king.turnMoved = -1;
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 7);
        rook.turnMoved = -1;
        board[0][7] = rook;
        
        board[0][5] = new Bishop(true, 0, 5); // Bishop blocks path

        int[][] moves = king.ValidMoves(board, false);
        assertFalse("Castle should be blocked by bishop", moveExists(moves, 0, 6));
    }
    
    @Test
    public void testCastleKingHasMoved() {
        King king = new King(true, 0, 4);
        king.turnMoved = 2; // King moved on turn 2
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 7);
        rook.turnMoved = -1;
        board[0][7] = rook;

        int[][] moves = king.ValidMoves(board, false);
        assertFalse("Castle should not be allowed if king has moved", moveExists(moves, 0, 6));
    }
    
    @Test
    public void testCastleRookHasMoved() {
        King king = new King(true, 0, 4);
        king.turnMoved = -1;
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 7);
        rook.turnMoved = 3; // Rook moved on turn 3
        board[0][7] = rook;

        int[][] moves = king.ValidMoves(board, false);
        assertFalse("Castle should not be allowed if rook has moved", moveExists(moves, 0, 6));
    }

    @Test
    public void testCastleThroughCheck() {
        King king = new King(true, 0, 4);
        king.turnMoved = -1;
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 7);
        rook.turnMoved = -1;
        board[0][7] = rook;
        
        // Black rook attacks [0,5] (f1), the square the king must pass through
        board[7][5] = new Rook(false, 7, 5);

        int[][] moves = king.ValidMoves(board, true); // checkDiscoved = true
        assertFalse("Castle should not be allowed when moving through check", moveExists(moves, 0, 6));
    }
    
    @Test
    public void testCastleWhileInCheck() {
        King king = new King(true, 0, 4);
        king.turnMoved = -1;
        board[0][4] = king;

        Rook rook = new Rook(true, 0, 7);
        rook.turnMoved = -1;
        board[0][7] = rook;
        
        // Black rook attacks the King directly
        board[7][4] = new Rook(false, 7, 4);

        int[][] moves = king.ValidMoves(board, true); // checkDiscoved = true
        assertFalse("Castle should not be allowed while in check", moveExists(moves, 0, 6));
    }
}