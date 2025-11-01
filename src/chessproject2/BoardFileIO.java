package chessproject2;

import chessproject2.ChessDB.ReadGameDB;
import chessproject2.Pieces.Piece;
import chessproject2.Pieces.PieceFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BoardFileIO manages creating and loading a default chess board setup
 *
 * The default board state is saved in the default.txt it writes the standard
 * initial chess setup(white pieces at the bottom and black at the top) it can
 * reload the default board from file or recreate it if its missing
 *
 *
 */
public final class BoardFileIO {
            private BoardFileIO() {}
            
            //Returns the standard chess starting position. No DB/File IO
            public static Piece[][] loadDefaultBoard()
            {
                Piece[][] b = new Piece[8][8];
                //White back rank
                b[0][0] = PieceFactory.fromType("Rook",   true, 0, 0);
                b[0][1] = PieceFactory.fromType("Knight", true, 0, 1);
                b[0][2] = PieceFactory.fromType("Bishop", true, 0, 2);
                b[0][3] = PieceFactory.fromType("Queen",  true, 0, 3);
                b[0][4] = PieceFactory.fromType("King",   true, 0, 4);
                b[0][5] = PieceFactory.fromType("Bishop", true, 0, 5);
                b[0][6] = PieceFactory.fromType("Knight", true, 0, 6);
                b[0][7] = PieceFactory.fromType("Rook",   true, 0, 7);
                
                //White pawns
                for (int c = 0; c < 8; c++) b[1][c] = PieceFactory.fromType("Pawn", true, 1, c);
                
                //Black Pawns 
                for (int c = 0; c < 8; c++) b[6][c] = PieceFactory.fromType("Pawn", false, 6, c);
                
                //Black back rank
                b[0][0] = PieceFactory.fromType("Rook",   false, 7, 0);
                b[0][1] = PieceFactory.fromType("Knight", false, 7, 1);
                b[0][2] = PieceFactory.fromType("Bishop", false, 7, 2);
                b[0][3] = PieceFactory.fromType("Queen",  false, 7, 3);
                b[0][4] = PieceFactory.fromType("King",   false, 7, 4);
                b[0][5] = PieceFactory.fromType("Bishop", false, 7, 5);
                b[0][6] = PieceFactory.fromType("Knight", false, 7, 6);
                b[0][7] = PieceFactory.fromType("Rook",   false, 7, 7);
                
                return b;
            }
}
