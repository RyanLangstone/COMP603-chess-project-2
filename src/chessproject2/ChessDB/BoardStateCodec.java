/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2.ChessDB;

import chessproject2.Pieces.Piece;
import chessproject2.Pieces.*;

/**
 *
 * @author Explosive
 */
public class BoardStateCodec {

    public static String encode(Piece[][] board) {
        StringBuilder sb = new StringBuilder(1024);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = board[row][col];
                if (p == null) {
                    sb.append("(null)");
                } else {
                    //Format: (Type, true|false)
                    sb.append('(').append(p.type()).append(",").append(p.isWhite()).append(",").append(p.turnMoved).append(')');
                }
            }
            if (row < 7) {
                sb.append('\n'); //ReadGameDB splits by newline/semicolon
            }
        }
        return sb.toString();
    }

    //Standard initial position with white at the bottom and black at the top
    public static Piece[][] initialBoardArray() {
        Piece[][] b = new Piece[8][8];
        //white
        b[0][0] = new Rook(true, 0, 0);
        b[0][1] = new Knight(true, 0, 1);
        b[0][2] = new Bishop(true, 0, 2);
        b[0][3] = new Queen(true, 0, 3);
        b[0][4] = new King(true, 0, 4);
        b[0][5] = new Bishop(true, 0, 5);
        b[0][6] = new Knight(true, 0, 6);
        b[0][7] = new Rook(true, 0, 7);
        for (int c = 0; c < 8; c++) {
            b[1][c] = new Pawn(true, 1, c);
        }
        //black
        for (int c = 0; c < 8; c++) {
            b[6][c] = new Pawn(false, 6, c);
        }
        b[7][0] = new Rook(false, 7, 0);
        b[7][1] = new Knight(false, 7, 1);
        b[7][2] = new Bishop(false, 7, 2);
        b[7][3] = new Queen(false, 7, 3);
        b[7][4] = new King(false, 7, 4);
        b[7][5] = new Bishop(false, 7, 5);
        b[7][6] = new Knight(false, 7, 6);
        b[7][7] = new Rook(false, 7, 7);
        return b;
    }
}
