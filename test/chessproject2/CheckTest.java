package chessproject2;

import chessproject2.GUI.BoardPanel;
import chessproject2.Pieces.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the core static methods of the Check class, focusing on check and checkmate detection.
 */
public class CheckTest {

    private Piece[][] board;

    @Before
    public void setUp() {
        // Create a new empty board for each test
        board = new Piece[8][8];
        // Since Check.board is static, we must assign our test board to it
        Check.board = board; 
    }

    // --- isSquareAttacked Tests ---

    @Test
    public void testIsSquareAttackedByPawn() {
        board[1][1] = new Pawn(true, 1, 1);
        
        board[2][0] = new Knight(false, 1, 1);
        assertTrue("Square [2,0] should be attacked by white pawn", Check.isSquareAttacked(2, 0, true, board));
        assertFalse("Square [2,2] should be attacked by white pawn", Check.isSquareAttacked(2, 2, true, board));
        assertTrue("Square [2,1] (straight) should not be 'attacked'", Check.isSquareAttacked(2, 1, true, board));
    }

    @Test
    public void testIsSquareAttackedByKnight() {
        board[3][3] = new Knight(true, 3, 3);
        assertTrue("Knight should attack [1,2]", Check.isSquareAttacked(1, 2, true, board));
        assertTrue("Knight should attack [5,4]", Check.isSquareAttacked(5, 4, true, board));
        assertFalse("Knight should not attack [3,4]", Check.isSquareAttacked(3, 4, true, board));
    }

    @Test
    public void testIsSquareAttackedByBishop() {
        board[3][3] = new Bishop(true, 3, 3);
        board[5][5] = new Pawn(false, 5, 5); // Blocker
        
        assertTrue("Bishop should attack [4,4]", Check.isSquareAttacked(4, 4, true, board));
        assertTrue("Bishop should attack [5,5] (capture)", Check.isSquareAttacked(5, 5, true, board));
        assertFalse("Bishop should be blocked by pawn at [5,5]", Check.isSquareAttacked(6, 6, true, board));
    }

    @Test
    public void testIsSquareAttackedByRook() {
        board[3][3] = new Rook(true, 3, 3);
        board[3][6] = new Pawn(true, 3, 6); // Friendly blocker
        
        assertTrue("Rook should attack [3,4]", Check.isSquareAttacked(3, 4, true, board));
        assertTrue("Rook should attack [7,3]", Check.isSquareAttacked(7, 3, true, board));
        assertFalse("Rook should be blocked by friendly pawn at [3,6]", Check.isSquareAttacked(3, 7, true, board));
    }

    @Test
    public void testIsSquareAttackedByQueen() {
        board[3][3] = new Queen(true, 3, 3);
        assertTrue("Queen should attack diagonally", Check.isSquareAttacked(5, 5, true, board));
        assertTrue("Queen should attack horizontally", Check.isSquareAttacked(3, 7, true, board));
    }

    @Test
    public void testIsSquareAttackedByKing() {
        board[3][3] = new King(true, 3, 3);
        assertTrue("King should attack [3,4]", Check.isSquareAttacked(3, 4, true, board));
        assertFalse("King should not attack [5,5]", Check.isSquareAttacked(5, 5, true, board));
    }

    // --- isCheckmate Tests ---

    @Test
    public void testIsCheckmateBackRankMate() {
        // White King is trapped on the back rank by its own pawns
        board[0][6] = new King(true, 0, 6);
        board[0][6].turnMoved = 2;
        board[1][5] = new Pawn(true, 1, 5);
        board[1][6] = new Pawn(true, 1, 6);
        board[1][7] = new Pawn(true, 1, 7);
        
        // Black Rook delivers checkmate
        board[0][0] = new Rook(false, 0, 0);

        assertTrue("This should be back rank checkmate for White", Check.isCheckmate(true, board));
    }

    @Test
    public void testIsCheckmateKingCanEscape() {
        // White King is in check...
        board[0][0] = new King(true, 0, 0);
        board[0][0].turnMoved = 2;
        board[0][7] = new Rook(false, 0, 7);
        
        // ...but can escape to [1,1]
        assertFalse("King should be able to escape to [1,1]", Check.isCheckmate(true, board));
    }

    @Test
    public void testIsCheckmateCanBlockCheck() {
        // White King is in check by a distant Rook...
        board[0][0] = new King(true, 0, 0);
        board[0][0].turnMoved = 2; // adding turn moved so it won't check for castling as king is not in starting square
        board[0][7] = new Rook(false, 0, 7);
        
        // ...but a White Bishop can block
        board[2][5] = new Bishop(true, 2, 5); // Can move to [0,3]

        assertFalse("Bishop should be able to block the check", Check.isCheckmate(true, board));
    }

    @Test
    public void testIsCheckmateCanCaptureAttacker() {
        // White King is in check by a Rook...
        board[0][0] = new King(true, 0, 0);
        board[0][0].turnMoved = 2;
        board[0][7] = new Rook(false, 0, 7);
        
        // ...but a White Queen can capture the Rook
        board[1][7] = new Queen(true, 1, 7);

        assertFalse("Queen should be able to capture the attacking rook", Check.isCheckmate(true, board));
    }

    @Test
    public void testIsCheckmateMate() {
        // Classic Fool's Mate setup
        board[0][4] = new King(true, 0, 4); // White King
        board[0][4].turnMoved = 2;
        board[7][4] = new King(false, 7, 4); // Black King
        board[7][4].turnMoved = 2;
        
        board[1][5] = new Pawn(true, 1, 5); // f2
        board[1][6] = new Pawn(true, 1, 6); // g2
        // White moves f2->f3
        board[2][5] = new Pawn(true, 1, 3);
        // White moves g2->g4
        board[3][6] = new Pawn(true, 3, 6);
        
        board[6][4] = new Pawn(false, 6, 4); // e7
        // Black moves e7->e5
        board[4][4] = new Pawn(false, 4, 4);
        
        // Black Queen delivers mate
        board[3][7] = new Queen(false, 0, 7); // Qh4

        // It is White's turn, and White is in checkmate
        assertTrue("This should be Mate", Check.isCheckmate(true, board));
    }
    
    @Test
    public void testIsNotCheckmateWhenNotInCheck() {
        // Standard starting board (or just kings)
        board[0][4] = new King(true, 0, 4);
        board[7][4] = new King(false, 7, 4);
        
        // King is not in check, so it cannot be checkmate
        assertFalse("Not in check, so not checkmate", Check.isCheckmate(true, board));
    }
}