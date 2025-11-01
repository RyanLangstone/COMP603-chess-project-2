
package chessproject2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the folder for the playermoves .txt files to be saved into 
 * 
 */ /*
public class PlayerMovesFileIO {
   private static final String MOVES_FOLDER = "moves";
   
   //Ensure folder exists
   private static void ensureMovesFolder()
   {
       File folder = new File(MOVES_FOLDER);
       if(!folder.exists())
       {
           folder.mkdirs();
       }
   }
   
   //Save a move to a file named after the game
   public static void saveMove(String gameName, String move)
   {
       ensureMovesFolder();
       File file = new File(MOVES_FOLDER, gameName + "_moves.txt");
       
       try (FileWriter fw = new FileWriter(file, true);
               BufferedWriter bw = new BufferedWriter(fw);
               PrintWriter out = new PrintWriter(bw))
       {
           out.println(move);
       } catch (IOException e)
       {
           System.out.println("Error saving move: " + e.getMessage());
       }
   }
   
   //Load all moves for a game
   public static List<String> loadMoves(String gameName)
   {
       ensureMovesFolder();
       File file = new File(MOVES_FOLDER, gameName + "_moves.txt");
       List<String> moves = new ArrayList<>();
       
       if(!file.exists())
       {
           return moves;
       }
       try(BufferedReader br = new BufferedReader(new FileReader(file)))
       {
           String line;
           while ((line = br.readLine()) != null)
           {
               moves.add(line);
           }
       } catch (IOException e)
       {
           System.out.println("Error loading moves: " + e.getMessage());
       }
       return moves;
   }
}
*/