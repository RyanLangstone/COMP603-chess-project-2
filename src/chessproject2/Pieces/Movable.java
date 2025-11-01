
package chessproject2.Pieces;

import chessproject2.Pieces.Piece;

/**
 * An interface for any object that can move on the chessboard.
 * contract for move validation.
 */
public interface Movable {
    public int[][] ValidMoves(Piece[][] board, boolean checkDiscoved);

}
