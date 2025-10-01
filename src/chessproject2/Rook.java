
package chessproject2;

/**
 * Child class of piece 
 * Has all of the Rook values and validMoves
 * 
 */
public class Rook extends Piece{

    public Rook(boolean isWhite, int row, int column) {
        this.isWhite = isWhite;
        this.position[0] = row;
        this.position[1] = column;
        this.type = "Rook";
        if (isWhite) {
            this.unicode = "\u2656";
        } else {
            this.unicode = "\u265C";
        }
    }

    @Override
    public int[][] ValidMoves(Piece[][] board) {
       int[][] possibleMoves = new int[64][2];
       int moveCount = 0;
       
       int[][] direction = {
           {1, 0}, {-1, 0}, {0, 1}, {0, -1}
       };
       for(int[] dir : direction)
       {
           int newRow = position[0] + dir[0];
           int newCol = position[1] + dir[1];
           
           while(newRow >= 0 && newRow <= 7 && newCol >= 0 && newCol <= 7)
           {
               Piece target = board[newRow][newCol];
               
               if(target == null)
               {
                   possibleMoves[moveCount][0] = newRow;
                   possibleMoves[moveCount][1] = newCol;
                   moveCount++;
               } else {
                   if(target.isWhite != this.isWhite)
                   {
                   possibleMoves[moveCount][0] = newRow;
                   possibleMoves[moveCount][1] = newCol;
                   moveCount++;
                   }
                   break;
               }
               newRow += dir[0];
               newCol += dir[1];
           }
       }
       int[][] validMoves = new int[moveCount][2];
       System.arraycopy(possibleMoves, 0, validMoves, 0, moveCount);
       return validMoves;
    }
    
}
