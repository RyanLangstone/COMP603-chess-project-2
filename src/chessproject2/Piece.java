
package chessproject2;



/**
 * Abstract piece class that has the universal attributes and methods for all the piece subclasses e.g Pawn, Rook, Bishop etc
 * Methods are public or protected depending on whether they are used outside of it or not for extra security
 */
 public abstract class Piece {

    protected String type, unicode;
    protected boolean isWhite;

    protected int[] position = new int[2];  // [row,column]

    protected int[] movement;
    
    public int turnMoved = -1;

    public String type() {
        return type;
    }
    
    public void setPosition(int row, int column) {
        position[0] = row;
        position[1] = column;
    }

    public boolean isWhite() {
      return isWhite;

    }

    public abstract int[][] ValidMoves(Piece[][] board);
    
}
