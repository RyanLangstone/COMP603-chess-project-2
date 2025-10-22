
package chessproject2;


import chessproject2.Pieces.Piece;
import static chessproject2.Game.blackName;
import static chessproject2.Game.board;
import static chessproject2.Game.gameName;
import static chessproject2.Game.turn;
import static chessproject2.Game.whiteName;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 *BoardFileWrite is responsible for saving the current state of the chess game into a text file
 * It handles:
 * Creating a saves folder if it doesnt exist
 * Writing game metadata (names, turn number)
 * Writing an 8x8 board state
 * Generating save file names
 * 
 * Save file format example:
 * #GameName: TestMatch
 * #WhitePlayer: Alice
 * #BlackPlayer: Bob
 * #Turn: 5
 * (Pawn,true)(null)(Pawn,false)....
 *
 * 
 */ /*
public class BoardFileWrite {
      private static final String SAVE_FOLDER = "saves";
      public static void WriteBoardFile(String saveFileName) {
          
          //Ensures the save folder exists
          File folder = new File(SAVE_FOLDER);
          if(!folder.exists())
          {
              folder.mkdir();
          }
          
          //Full file path: saves/<saveFileName>
          String fullPath = SAVE_FOLDER + "/" + saveFileName;
 
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(fullPath)))
        {
            //Writes game metadata
            pw.println("#GameName: " + gameName);
            pw.println("#WhitePlayer: " + whiteName);
            pw.println("#BlackPlayer: " + blackName);
            pw.println("#Turn: " + turn);
            
            //Write 8x8 board
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece p = board[row][col];
                    if (p != null) {
                        //Saves piece as (Type,isWhite)
                        pw.print("(" + p.type() + "," + p.isWhite() + ")");
                    } else {
                        //Otherwise it just prints an empty square
                        pw.print("(null)");
                    }

                }
                pw.println();

            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
      //Generates the next available save file name in the format game1, game2, game3
      //It scans the saves folder and finds the highest existing number, then returns the next one
      public static String getNextSaveFileName()
      {
          File folder = new File(SAVE_FOLDER);
          if(!folder.exists()) //Ensures the save folder exists
          {
              folder.mkdir();
          }
          int next = 1; //Starts numbering from 1
          for (File file : folder.listFiles())
          {
              //Looks through all the files in save
              String name = file.getName();
              if(name.startsWith("game") && name.endsWith(".txt")) //Only considers files with the name gameN.txt
              {
                  try {
                      //Extract the number part between game and txt
                      int n = Integer.parseInt(name.substring(4, name.length() - 4));
                      if (n >= next) //Keeps track of the highest used number
                      {
                          
                          next = n + 1;
                      }
                  } catch (NumberFormatException ignored) {} //Ignores files that dont follow the format
              }
          }
          return "game" + next + ".txt";
      }
}
*/