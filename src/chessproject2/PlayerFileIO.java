
package chessproject2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * PlayerFileIO handles reading and writing player data from/to a text file
 * 
 * The file is named players.TXT
 * Each line stores one player in the format
 * Name, Wins, Losses
 * 
 * Example file content
 * Alice, 3, 1
 * Bob, 0, 5
 * 
 * This class allows loading all players into memory and saving the updates back to the file by merging new data with existing data
 * 
 * 
 */
public class PlayerFileIO {
    private static final String FILE_NAME = "players.txt";
    
    public static HashMap<String, Player> loadPlayers()
    {
        
        //Loads all players from players.txt into a HashMap
        HashMap<String, Player> players = new HashMap<>();
        File file = new File(FILE_NAME);
        
        //If the file doesnt exist yet, return empty player list
        if (!file.exists())
        {
            return players;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            //Read each line until the end of the file
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] parts = line.split(", ");
                if (parts.length == 3)
                    //parts[0] = player name
                    //parts[1] = wins
                    //parts[2] = losses
                {
                    Player p = new Player(parts[0]);
                    for (int i = 0; i < Integer.parseInt(parts[1]); i++) 
                    {
                        p.addWin(); //Adds wins
                    }
                    for(int i = 0; i < Integer.parseInt(parts[2]); i++)
                    {
                        p.addLosses(); //Adds losses
                    }
                    //Stores players in the HashMap
                    players.put(p.getName(), p);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return players;
    }
    
    /* Saves updated players back to the players.txt file
    Steps:
    1. Load existing players from the file
    2. Merge new player data into existing ones
    3. Overwirtes players.txt with the merged list
    */
    public static void savePlayers(HashMap<String, Player> newPlayers)
    {
        //Step 1: Load existing players
        HashMap<String, Player> existingPlayers = loadPlayers();
        //Step 2: Merge new players into existing ones
        for (String name : newPlayers.keySet())
        {
            Player newPlayer = newPlayers.get(name);
            if (existingPlayers.containsKey(name))
            {
                //Updates existing players wins and losses
                Player existing = existingPlayers.get(name);
                for(int i = 0; i < newPlayer.getWins(); i++)
                {
                    //Adds new wins
                    existing.addWin();
                }
                for(int i = 0; i < newPlayer.getLosses(); i++)
                {
                    //Adds new losses
                    existing.addLosses();
                }
            } else {
                //Adds an entirely new player
                existingPlayers.put(name, newPlayer);
            }
        }
        //Step 3: Overwrite the file with the merged players list
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME)))
        {
            for (Player p : existingPlayers.values())
            {
                pw.println(p.getName() + ", " + p.getWins() + ", " + p.getLosses());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
