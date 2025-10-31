package chessproject2;

import chessproject2.Pieces.Piece;
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
public class BoardFileIO {

    private static final String DEFAULT_FILE = "default.txt";

    public static void writeDefaultBoard() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DEFAULT_FILE, false))) {
            //White back rank
            pw.println("(Rook,true)(Knight,true)(Bishop,true)(Queen,true)(King,true)(Bishop,true)(Knight,true)(Rook,true)");

            //White front rank
            pw.println("(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)(Pawn,true)");
            for (int i = 0; i < 4; i++) {
                //Empty rows
                pw.println("(null)(null)(null)(null)(null)(null)(null)(null)");
            }
            //Black front rank
            pw.println("(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)(Pawn,false)");

            //Black back rank
            pw.println("(Rook,false)(Knight,false)(Bishop,false)(Queen,false)(King,false)(Bishop,false)(Knight,false)(Rook,false)");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Loads the default board from default.txt
    //If the file doesnt exist, it prints a warning and calls makeBoard.MakeBoard to manually generate it and writes a new default.txt for future use
    public static Piece[][] loadDefaultBoard() {
        try {
            return ReadBoardFileTest.ReadBoardFile(DEFAULT_FILE); //Tries loading default board from the file
        } catch (Exception e) {
            System.out.println("default.txt not found, generating board manually...");
            //Fallback just incase the default file isnt made
            makeBoard.MakeBoard();
            writeDefaultBoard();
            try {
                return ReadBoardFileTest.ReadBoardFile(DEFAULT_FILE);
            } catch (IOException ex) {
                Piece[][] failBoard = new Piece[8][8];
                return failBoard; //should never happen as above statments write the file, but need it to compile as the file has been declared to throws IOException
            }
        }
    }
}
