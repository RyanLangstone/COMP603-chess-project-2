package chessproject2;

import java.util.Arrays;

/**
 **Child class of piece Has all of the Pawn values and validMoves and special
 * moves such as double move, en passant and diagonal captures
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

        int[][] validMoves = new int[3][2]; // Only forward movement, no diagonal
        int moveCount = 0;

        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 1 : 6;

        int newRow = position[0] + direction;
        int column = position[1];
        if (newRow >= 0 && newRow <= 7 && board[newRow][column] == null) {
            if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, !isWhite, changeBoard(newRow, column, board))) { // because java doesnt does short-circuit evaluation, the long isSquareAttacked will only be called when needed
                //Forward movements
                validMoves[moveCount][0] = newRow;
                validMoves[moveCount][1] = column;
                moveCount++;
            }
            if (position[0] == startRow) {
                int twoRow = position[0] + 2 * direction;
                if (twoRow >= 0 && twoRow <= 7 && board[twoRow][position[1]] == null) {
                    if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, !isWhite, changeBoard(twoRow, column, board))) {
                        //fist double move
                        validMoves[moveCount][0] = twoRow;
                        validMoves[moveCount][1] = column;
                        moveCount++;
                    }

                }
            }
        }
        if (newRow >= 0 && newRow <= 7) {
            // capture movments + En Passant
            if (column + 1 <= 7 && board[newRow][column + 1] != null) {
                if (board[newRow][column + 1].isWhite() != isWhite) {
                    if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, isWhite, changeBoard(newRow, column + 1, board))) {
                        validMoves[moveCount][0] = newRow;
                        validMoves[moveCount][1] = column + 1;
                        moveCount++;
                    }
                }
            } else if (column + 1 <= 7 && board[position[0]][column + 1] != null) {
                if (board[position[0]][column + 1].turnMoved == Game.getTurn() - 1 && board[position[0]][column + 1].isWhite() != isWhite && board[position[0]][column + 1].type == "Pawn") {
                    if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, isWhite, changeBoard(newRow, column + 1, board, "enPassant"))) {
                        validMoves[moveCount][0] = newRow;
                        validMoves[moveCount][1] = column + 1;
                        moveCount++;
                    }
                }
            }
            if (column - 1 >= 0 && board[newRow][column - 1] != null) {
                if (board[newRow][column - 1].isWhite() != isWhite) {
                    if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, isWhite, changeBoard(newRow, column - 1, board))) {
                        validMoves[moveCount][0] = newRow;
                        validMoves[moveCount][1] = column - 1;
                        moveCount++;
                    }
                }
            } else if (column - 1 >= 0 && board[position[0]][column - 1] != null) {
                if (board[position[0]][column - 1].turnMoved == Game.getTurn() - 1 && board[position[0]][column - 1].isWhite() != isWhite && board[position[0]][column - 1].type == "Pawn") {
                    if (checkDiscoved == false || !Game.isSquareAttacked(kingRow, kingCol, isWhite, changeBoard(newRow, column - 1, board, "enPassant"))) {
                        validMoves[moveCount][0] = newRow;
                        validMoves[moveCount][1] = column - 1;
                        moveCount++;
                    }
                }
            }

        }

        return Arrays.copyOf(validMoves, moveCount);
    }

}
