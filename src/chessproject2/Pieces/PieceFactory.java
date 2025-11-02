package chessproject2.Pieces;

/**
 * PieceFactory is a factory class that creates chess piece objects based on a
 * given string type
 *
 * It converts text data into actual piece objects Each piece is initialized
 * with Color(isWhite true for white, false for black) and Its current board
 * position (row, col)
 *
 *
 */
public class PieceFactory {

    public static Piece fromType(String type, boolean isWhite, int row, int col) {
        switch (type) {
            case "Pawn":
                return new Pawn(isWhite, row, col);
            case "Rook":
                return new Rook(isWhite, row, col);
            case "Knight":
                return new Knight(isWhite, row, col);
            case "Bishop":
                return new Bishop(isWhite, row, col);
            case "Queen":
                return new Queen(isWhite, row, col);
            case "King":
                return new King(isWhite, row, col);
            default:
                return null;
        }
    }
}
