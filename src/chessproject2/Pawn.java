
package chessproject2;

import java.util.Arrays;

/**
 **Child class of piece 
 * Has all of the Pawn values and validMoves and special moves such as double move, en passant and diagonal captures
 * 
 */
public class Pawn extends Piece {

    public Pawn(boolean isWhite, int row, int column) {
        this.isWhite = isWhite;
        this.position[0] = row;
        this.position[1] = column;
        this.type = "Pawn";
        if (isWhite) {
            this.unicode = "\u2659";
        } else {
            this.unicode = "\u265F";
        }
    }

    @Override
    public int[][] ValidMoves(Piece[][] board) {
        int[][] validMoves = new int[3][2]; // Only forward movement, no diagonal
        int moveCount = 0;

        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 1 : 6;

        int newRow = position[0] + direction;
        int column = position[1];
        if (newRow >= 0 && newRow <= 7 && board[newRow][column] == null) {
            //Forward movements
            validMoves[moveCount][0] = newRow;
            validMoves[moveCount][1] = column;
            moveCount++;
            if (position[0] == startRow) {
                int twoRow = position[0] + 2 * direction;
                if (twoRow >= 0 && twoRow <= 7 && board[twoRow][position[1]] == null) {
                    validMoves[moveCount][0] = twoRow;
                    validMoves[moveCount][1] = position[1];
                    moveCount++;
                }
            }
        }
        if (newRow >= 0 && newRow <= 7){
            // capture movments + En Passant
            if (column + 1 <= 7 && board[newRow][column + 1] != null) {
                if (board[newRow][column + 1].isWhite() != isWhite) {
                    validMoves[moveCount][0] = newRow;
                    validMoves[moveCount][1] = column + 1;
                    moveCount++;
                }
            } else if (column + 1 <= 7 && board[position[0]][column + 1] != null) {
                if (board[position[0]][column + 1].turnMoved == Game.getTurn() - 1 && board[position[0]][column + 1].isWhite() != isWhite && board[position[0]][column + 1].type == "Pawn") {
                    validMoves[moveCount][0] = newRow;
                    validMoves[moveCount][1] = column + 1;
                    moveCount++;
                }
            }
            if (column - 1 >= 0 && board[newRow][column - 1] != null) {
                if (board[newRow][column - 1].isWhite() != isWhite) {

                    validMoves[moveCount][0] = newRow;
                    validMoves[moveCount][1] = column - 1;
                    moveCount++;
                }
            } else if (column - 1 >= 0 && board[position[0]][column - 1] != null) {
                if (board[position[0]][column - 1].turnMoved == Game.getTurn() - 1 && board[position[0]][column - 1].isWhite() != isWhite && board[position[0]][column - 1].type == "Pawn") {
                    validMoves[moveCount][0] = newRow;
                    validMoves[moveCount][1] = column - 1;
                    moveCount++;
                }
            }            

        }

        return Arrays.copyOf(validMoves, moveCount);
    }

}
