package chessproject2.Pieces;

import chessproject2.Game;
import java.util.Arrays;

/**
 * Child class of piece Has all of the Knight values and validMoves
 *
 */
public class Knight extends Piece {

    private int[][] movement = new int[][]{{1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}}; // keeping it as a 2d as although it only has 1 option, all others have multiple and keep it consistant

    public Knight(boolean isWhite, int row, int column) {
        this.isWhite = isWhite;
        this.position[0] = row;
        this.position[1] = column;
        this.type = "Knight";
        if (isWhite) {
            this.unicode = "\u2658";
        } else {
            this.unicode = "\u265E";
        }
    }

    @Override
    public int[][] ValidMoves(Piece[][] board, boolean checkDiscoved) {
        int kingRow = -1, kingCol = -1;
        if (checkDiscoved == true) {

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = board[r][c];
                    if (p != null && p.type.equals("King") && p.isWhite == isWhite) {
                        kingRow = r;
                        kingCol = c;
                    }
                }
            }
        }

        int[][] validMoves = new int[movement.length][2];
        int moveCount = 0;
        int positionX = position[0];
        int positionY = position[1];
        for (int[] option : movement) {
            int newRow = position[0] + option[0];
            int newCol = position[1] + option[1];

            if (newRow >= 0 && newRow <= 7 && newCol >= 0 && newCol <= 7) {

                Piece target = board[newRow][newCol];

                if (target == null || target.isWhite != this.isWhite) {
                    if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, !isWhite, changeBoard(newRow, newCol, board))) {
                        validMoves[moveCount][0] = newRow;
                        validMoves[moveCount][1] = newCol;
                        moveCount++;
                    }
                }
            }
        }
        return Arrays.copyOf(validMoves, moveCount);
    }
    public static String getUnicode(boolean isWhite){
        return isWhite ? "\u2658": "\u265E";
    }

}
