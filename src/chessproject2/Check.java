package chessproject2;

import chessproject2.ChessDB.ReadGameDB;
import chessproject2.ChessDB.PlayerDB;

import chessproject2.Pieces.Piece;
import chessproject2.Pieces.PieceConversion;
import chessproject2.Pieces.PieceFactory;
import static chessproject2.Pieces.PieceConversion.chessNotationToIndex;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Main game logic for chess Handles initialization, menus, players, board
 * rendering, move validation, and special rules
 *
 * @author RyanL and Yaacoub
 */
public class Check {

    //8x8 chess board, stores Piece objects or nul
    
    //Current game metadata
    public static String gameName = ""; //Current game name
    public static String whiteName = ""; // White players name
    public static String blackName = ""; //Black players name
    public static int turn = 0; // Current turn counter (even = white, odd = black)    

    public static boolean isCheckmate(boolean isWhiteTurn, Piece[][] board) {
        // 1. Locate the king
        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.type.equals("King") && p.isWhite == isWhiteTurn) {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        if (kingRow == -1) {
            // no king found (shouldn’t happen)
            return true;
        }

        // 2. Check if king is under attack
        if (!isSquareAttacked(kingRow, kingCol, !isWhiteTurn, board)) {
            return false; // not in check → no checkmate
        }

        // 3. Try all king moves
        int[][] kingMoves = board[kingRow][kingCol].ValidMoves(board, true);
        for (int[] move : kingMoves) {
            int newRow = move[0], newCol = move[1];
            Piece backup = board[newRow][newCol];
            board[newRow][newCol] = board[kingRow][kingCol];
            board[kingRow][kingCol] = null;

            boolean stillInCheck = isSquareAttacked(newRow, newCol, !isWhiteTurn, board);

            // undo move
            board[kingRow][kingCol] = board[newRow][newCol];
            board[newRow][newCol] = backup;

            if (!stillInCheck) {
                return false; // king can escape
            }
        }

        // 4. Try all moves from friendly pieces
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null || p.isWhite != isWhiteTurn || p.type.equals("King")) {
                    continue;
                }

                int[][] moves = p.ValidMoves(board, true);
                for (int[] move : moves) {
                    int newRow = move[0], newCol = move[1];
                    Piece backup = board[newRow][newCol];
                    board[newRow][newCol] = p;
                    board[r][c] = null;

                    // after move, is king still in check?
                    boolean stillInCheck = isSquareAttacked(kingRow, kingCol, !isWhiteTurn, board);

                    // undo move
                    board[r][c] = p;
                    board[newRow][newCol] = backup;

                    if (!stillInCheck) {
                        return false; // a block or capture worked
                    }
                }
            }
        }

        // 5. No escape, no block, no capture → checkmate
        return true;

    }

    /**
     * Utility to check if a square is attacked by enemy pieces
     */
    public static boolean isSquareAttacked(int row, int col, boolean byWhite, Piece[][] board) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null || p.isWhite != byWhite) {
                    continue;
                }

                int[][] moves = p.ValidMoves(board, false);
                for (int[] m : moves) {
                    if (m[0] == row && m[1] == col) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
