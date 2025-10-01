
package chessproject2;

/**
 *Child class of piece 
 * Has all of the King values and validMoves and special moves of castling
 * 
 */
public class King extends Piece {

    public King(boolean isWhite, int row, int column) {
        this.isWhite = isWhite;
        this.position[0] = row;
        this.position[1] = column;
        this.type = "King";
        if (isWhite) {
            this.unicode = "\u2654";
        } else {
            this.unicode = "\u265A";
        }
    }

    @Override
    public int[][] ValidMoves(Piece[][] board) {
        int[][] possibleMoves = new int[8][2];
        int moveCount = 0;

        int[][] direction = {
            {-1, 1}, {-1, 0}, {-1, 1}, //diagonal + up
            {0, -1}, {0, 1}, //left, right
            {1, -1}, {1, 0}, {1, 1} //diagonal + down
        };
        int row = position[0];
        int col = position[1];

        for (int[] dir : direction) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            //Checks to make sure the new move is within the board
            if (newRow >= 0 && newRow <= 7 && newCol >= 0 && newCol <= 7) {
                Piece target = board[newRow][newCol];

                if (target == null || target.isWhite != this.isWhite) {
                    possibleMoves[moveCount][0] = newRow;
                    possibleMoves[moveCount][1] = newCol;
                    moveCount++;
                }
            }
        }
        // checks for castling
        if (turnMoved == -1) {
            if (board[row][0] != null) {
                if (board[row][0].turnMoved == -1 && board[row][1] == null && board[row][2] == null && board[row][3] == null) {
                    possibleMoves[moveCount][0] = row;
                    possibleMoves[moveCount][1] = col-2;
                    moveCount++;
                }
            }
            if (board[row][7] != null) {
                if (board[row][7].turnMoved == -1 && board[row][6] == null && board[row][5] == null) {
                    possibleMoves[moveCount][0] = row;
                    possibleMoves[moveCount][1] = col+2;
                    moveCount++;
                }
            }
        }

        return java.util.Arrays.copyOf(possibleMoves, moveCount);
    }

}
