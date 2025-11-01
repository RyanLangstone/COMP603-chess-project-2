package chessproject2.Pieces;

/**
 * Abstract piece class that has the universal attributes and methods for all
 * the piece subclasses e.g Pawn, Rook, Bishop etc Methods are public or
 * protected depending on whether they are used outside of it or not for extra
 * security
 */
 public abstract class Piece implements Movable{

    public String type, unicode;
    public boolean isWhite;

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

    protected Piece[][] changeBoard(int newRow, int newCol, Piece[][] board) {//
        return changeBoard(newRow, newCol, board, "");
    }

    /**
     * Creates a deep copy of the board and simulates moving this piece to a new
     * location on that new board. This is used for checking for check/checkmate
     * without altering the real game state.
     *
     * @param board The current game board.
     * @param newRow The target row for the simulated move.
     * @param newCol The target column for the simulated move.
     * @param specialMove enPassant, ksCastle or qsCastle
     * @return A new Piece[][] array representing the board state *after* the
     * move.
     */
    protected Piece[][] changeBoard(int newRow, int newCol, Piece[][] board, String specialMove) { // used for checking if the board in that state is valid
        Piece[][] newBoard = new Piece[8][8];

        // 1. Create a deep copy of the board
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece originalPiece = board[r][c];
                if (originalPiece != null) {
                    // Use the PieceFactory to create a new instance so that it isnt referencing the main board values
                    Piece newPiece = PieceFactory.fromType(originalPiece.type(), originalPiece.isWhite(), r, c);
                    // Copy the move history, (castling/en passant checks)
                    newPiece.turnMoved = originalPiece.turnMoved;
                    newBoard[r][c] = newPiece;
                } else {
                    newBoard[r][c] = null;
                }
            }
        }
        if (specialMove.equals("enPassant")) {
            newBoard[this.position[0]][newCol] = null;
        } else if (specialMove.equals("ksCastle")) {  //king side castle
            // moves the rook
            newBoard[position[0]][5] = newBoard[position[0]][7];
            newBoard[position[0]][7] = null;
        } else if (specialMove.equals("qsCastle")) {  //queen side castle
            // moves the rook
            newBoard[position[0]][3] = newBoard[position[0]][0];
            newBoard[position[0]][0] = null;
        }

        // moves the current piece
        newBoard[newRow][newCol] = newBoard[position[0]][position[1]];
        newBoard[newRow][newCol].setPosition(newRow, newCol);
        newBoard[this.position[0]][this.position[1]] = null;

        return newBoard;
    }

    /**
     **
     * @param checkDiscoved is for wether or not to check if the move makes a
     * discovered check, which won't be called when method is called as apart of
     * the check itself (stop recursion)
     *
     */
    @Override
    public abstract int[][] ValidMoves(Piece[][] board, boolean checkDiscoved);    
}
