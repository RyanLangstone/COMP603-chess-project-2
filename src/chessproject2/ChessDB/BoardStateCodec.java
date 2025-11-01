/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2.ChessDB;

import chessproject2.Pieces.Piece;

/**
 *
 * @author Explosive
 */
public final class BoardStateCodec {
        private BoardStateCodec()  {}
        
        public static String encode(Piece[][] board)
        {
            if(board == null) return "";
            StringBuilder sb = new StringBuilder(1024);
            for (int row = 0; row < 8; row++)
            {
                for(int col = 0; col < 8; col++)
                {
                    Piece p = board[row][col];
                    if(p==null)
                    {
                        sb.append("(null)");
                    } else {
                        //Format: (Type, true|false)
                        sb.append('(').append(p.type()).append(", ").append(p.isWhite()).append(')');
                    }
                    if(col < 7) sb.append(' ');
                }
                if(row < 7) sb.append('\n'); //ReadGameDB splits by newline/semicolon
            }
            return sb.toString();
        }
}
