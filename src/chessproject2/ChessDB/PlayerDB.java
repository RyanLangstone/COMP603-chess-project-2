
package chessproject2.ChessDB;

import chessproject2.Player;
import java.sql.*;
import java.util.HashMap;

/**
 * PlayerDB handles reading and writing player data from/to a database
 
 Each column stores one player in the format
 Name, Wins, Losses
 
 Example file content
 Alice, 3, 1
 Bob, 0, 5
 
 This class allows loading all players into memory and saving the updates back to the file by merging new data with existing data
 * 
 * 
 */
public class PlayerDB {
    
    public static HashMap<String, Player> loadPlayers()
    {
        HashMap<String, Player> players = new HashMap<>();
        
        try (Connection conn = ChessDatabase.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM players"))
        {
            while (rs.next())
            {
                String name = rs.getString("name");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                
                Player p = new Player(name);
                for (int i = 0; i < wins; i++) p.addWin();
                for (int i = 0; i < losses; i++) p.addLosses();
                players.put(name, p);
            }
        } catch (SQLException e)
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
    /* Saves updated players back to the players.txt file
    Steps:
    1. Load existing players from the file
    2. Merge new player data into existing ones
    3. Overwirtes players.txt with the merged list
    */
    public static void savePlayers(Player player)
    {
      try (Connection conn = ChessDatabase.getConnection())
      {
          //Try update first
          PreparedStatement update = conn.prepareStatement(
                  "UPDATE players SET wins=?, losses=? WHERE name=?"
          );
          update.setInt(1, player.getWins());
          update.setInt(2, player.getLosses());
          update.setString(3, player.getName());
          int affected = update.executeUpdate();
          
          //If no rows are updated, insert the values
          if(affected == 0)
          {
              PreparedStatement insert = conn.prepareStatement(
              "INSERT INTO players(name, wins, losses) VALUES (?,?,?)"
              );
              insert.setString(1, player.getName());
              insert.setInt(2, player.getWins());
              insert.setInt(3, player.getLosses());
              insert.executeUpdate();
          }
      } catch (SQLException e)
      {
          e.printStackTrace();
      }
    }
}
