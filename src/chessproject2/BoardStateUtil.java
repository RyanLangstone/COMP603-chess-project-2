/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import chessproject2.Pieces.Piece;

public final class BoardStateUtil {

    private BoardStateUtil() {}

    public static String boardToState(Piece[][] board) {
        StringBuilder sb = new StringBuilder(1024);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null) {
                    sb.append("(null)");
                } else {
                    sb.append('(').append(p.type).append(',').append(p.isWhite).append(')');
                }
            }
            if (r < 7) sb.append('\n');
        }
        return sb.toString();
    }

    public static String defaultBoardState() {
        String[] rows = new String[] {
            "(Rook,true)(Knight,true)(Bishop,true)(Queen,true)(King,true)(Bishop,true)(Knight,true)(Rook,true)",
            "(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)",
            "(null)(null)(null)(null)(null)(null)(null)(null)",
            "(null)(null)(null)(null)(null)(null)(null)(null)",
            "(null)(null)(null)(null)(null)(null)(null)(null)",
            "(null)(null)(null)(null)(null)(null)(null)(null)",
            "(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)",
            "(Rook,false)(Knight,false)(Bishop,false)(Queen,false)(King,false)(Bishop,false)(Knight,false)(Rook,false)"
        };
        return String.join("\n", rows);
    }
}
