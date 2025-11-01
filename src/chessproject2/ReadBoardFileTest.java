package chessproject2;

import chessproject2.Pieces.PieceFactory;
import static chessproject2.Game.board; //Import the shared board object from Game
import chessproject2.Pieces.Piece;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for reading a saved chess board from a text file and
 * loading it into the current game state
 *
 * Expected file format: Metadata lines start with # such as #GameName,
 * #WhitePlayer, #BlackPlayer, #Turn -8 lines, each representing a row of the
 * chess board -Each square is written as (PieceType, isWhite) or (null)
 * -Example row: (Rook, true) (Knight, true) (null)
 *
 * This class reconstructs the board state by parsing the file line by line
 *
 */
public class ReadBoardFileTest {

    public static Piece[][] ReadBoardFile(String filename) throws IOException {
        Piece[][] localBoard = new Piece[8][8];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0; //Track which row of the board were filling 

            //Read file line by line until the end of file or row 8 is reached
            while ((line = br.readLine()) != null && row < 8) {
                //Ensures proper splitting of the piece tokens:
                //Input is (Pawn,true)(Rook, false)
                // Split on ")(" but keep brackets
                line = line.replace(")(", ") ("); // add space so split works clean
                String[] tokens = line.trim().split("\\s+");

                // Handles metadata lines
                if (line.startsWith("#GameName: ")) {
                    Game.gameName = line.substring(11).trim(); //Extracts game name
                    continue;
                }

                if (line.startsWith("#WhitePlayer: ")) {
                    Game.whiteName = line.substring(14).trim(); //Extract white players name
                    continue;
                }

                if (line.startsWith("#BlackPlayer: ")) {
                    Game.blackName = line.substring(14).trim(); //Extract black players name 
                    continue;
                }

                if (line.startsWith("#Turn: ")) {
                    Game.turn = Integer.parseInt(line.substring(7).trim()); //Extract current turn
                    continue;
                }

                //Handles board rows
                for (int col = 0; col < 8 && col < tokens.length; col++) {
                    String token = tokens[col].trim();

                    if (token.equals("(null)")) {
                        //Empty square
                        localBoard[row][col] = null;

                        board[row][col] = null;
                    } else {
                        // remove brackets -> Pawn,true
                        token = token.substring(1, token.length() - 1);
                        String[] parts = token.split(",");
                        String type = parts[0]; //Piece type (Pawn, Rook, etc)
                        boolean isWhite = Boolean.parseBoolean(parts[1]); //Piece color

                        //Recreate the piece object at row and column
                        board[row][col] = PieceFactory.fromType(type, isWhite, row, col);
                        localBoard[row][col] = PieceFactory.fromType(type, isWhite, row, col);

                    }
                }
                row++; //Move to next board row
            }

            return localBoard;
        }
    }
}
