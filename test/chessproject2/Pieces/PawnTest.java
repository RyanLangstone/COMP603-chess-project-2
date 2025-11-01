package chessproject2.Pieces;

import chessproject2.GUI.BoardPanel;
import chessproject2.Game;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests Pawn.java, focusing on initial double moves, captures, en passant, and king safety.
 */
public class PawnTest {

    private Piece[][] board;

    @Before
    public void setUp() {
        board = new Piece[8][8];
        Game.board = board; // Assign to the static field
        BoardPanel.turn = 0; // Reset static turn counter
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
    public void testPawnInitialDoubleMove() {
        Pawn pawn = new Pawn(true, 1, 1); // White pawn at b2
        board[1][1] = pawn;

        int[][] moves = pawn.ValidMoves(board, false);
        assertEquals(2, moves.length);
        assertTrue("Should have 1-square move", moveExists(moves, 2, 1));
        assertTrue("Should have 2-square move", moveExists(moves, 3, 1));
    }
    
    @Test
    public void testPawnInitialDoubleMoveBlocked() {
        Pawn pawn = new Pawn(true, 1, 1); // White pawn at b2
        board[1][1] = pawn;
        board[3][1] = new Pawn(false, 3, 1); // Black pawn at b4 blocks

        int[][] moves = pawn.ValidMoves(board, false);
        assertEquals("Double move should be blocked", 1, moves.length);
        assertTrue("Should only have 1-square move", moveExists(moves, 2, 1));
    }

    @Test
    public void testPawnSingleMove() {
        Pawn pawn = new Pawn(true, 2, 1); // White pawn at b3
        pawn.turnMoved = 1; // Mark as having moved
        board[2][1] = pawn;

        int[][] moves = pawn.ValidMoves(board, false);
        assertEquals("Pawn that has moved should only have 1 move", 1, moves.length);
        assertTrue("Should only have 1-square move", moveExists(moves, 3, 1));
    }
    
    @Test
    public void testPawnDiagonalCapture() {
        Pawn pawn = new Pawn(true, 2, 1); // White pawn at b3
        board[2][1] = pawn;
        board[3][0] = new Pawn(false, 3, 0); // Black pawn at a4
        board[3][2] = new Pawn(false, 3, 2); // Black pawn at c4

        int[][] moves = pawn.ValidMoves(board, false);
        assertEquals("Should have 1 forward and 2 capture moves", 3, moves.length);
        assertTrue("Should be able to capture at [3,0]", moveExists(moves, 3, 0));
        assertTrue("Should be able to capture at [3,2]", moveExists(moves, 3, 2));
        assertTrue("Should be able to move forward to [3,1]", moveExists(moves, 3, 1));
    }
    
    @Test
    public void testPawnCaptureFriendlyFire() {
        Pawn pawn = new Pawn(true, 2, 1); // White pawn at b3
        board[2][1] = pawn;
        board[3][0] = new Pawn(true, 3, 0); // FRIENDLY pawn at a4

        int[][] moves = pawn.ValidMoves(board, false);
        assertFalse("Should not be able to capture friendly pawn", moveExists(moves, 3, 0));
    }

    @Test
    public void testPawnMoveLeavesKingInCheck() {
        // Pinned pawn
        board[0][4] = new King(true, 0, 4);
        Pawn pawn = new Pawn(true, 1, 4); // Pawn directly in front of king
        board[1][4] = pawn;
        board[7][4] = new Rook(false, 7, 4); // Black rook pinning the pawn

        int[][] moves = pawn.ValidMoves(board, true); // checkDiscoved = true
        
        assertEquals("Pinned pawn should have 0 moves", 0, moves.length);
    }

    // --- En Passant Tests ---

    @Test
    public void testPawnEnPassantLegal() {
        // 1. White pawn is in position
        Pawn whitePawn = new Pawn(true, 4, 4); // White at e5
        whitePawn.turnMoved = 1;
        board[4][4] = whitePawn;

        // 2. Black pawn is on starting rank
        Pawn blackPawn = new Pawn(false, 6, 3); // Black at d7
        board[6][3] = blackPawn;

        // 3. Simulate Black's double move
        blackPawn.turnMoved = 5; // Black moves on turn 5
        board[4][3] = blackPawn; // Black is now at d5
        board[6][3] = null;

        // 4. Set global turn to the *next* turn
        // Your code checks `board[...].turnMoved == BoardPanel.turn - 1`
        BoardPanel.turn = 6; 

        // 5. Get moves for White
        int[][] moves = whitePawn.ValidMoves(board, false);

        assertTrue("White pawn at e5 should be able to en passant capture d5", moveExists(moves, 5, 3));
    }
    
    @Test
    public void testPawnEnPassantTooLate() {
        // 1. White pawn
        Pawn whitePawn = new Pawn(true, 4, 4); // White at e5
        whitePawn.turnMoved = 1;
        board[4][4] = whitePawn;

        // 2. Black pawn
        Pawn blackPawn = new Pawn(false, 6, 3); // Black at d7
        board[6][3] = blackPawn;

        // 3. Simulate Black's double move
        blackPawn.turnMoved = 5; // Black moves on turn 5
        board[4][3] = blackPawn;
        board[6][3] = null;

        // 4. Set global turn, but it's TOO LATE
        BoardPanel.turn = 7; // Black moved on 5, it is now turn 7. En passant only valid on turn 6.

        // 5. Get moves for White
        int[][] moves = whitePawn.ValidMoves(board, false);

        assertFalse("En passant should not be legal after one turn has passed", moveExists(moves, 5, 3));
    }
}