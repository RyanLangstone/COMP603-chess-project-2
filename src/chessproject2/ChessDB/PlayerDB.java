
package chessproject2.ChessDB;

import chessproject2.Player;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
        
        Connection conn = ChessDatabase.getConnection();
        try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name,wins,losses FROM players ORDER BY name"))
        {
            while (rs.next())
            {
                String name = rs.getString(1);
                int wins = rs.getInt(2);
                int losses = rs.getInt(3);
                
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
    public static void savePlayers(Map<String, Player> players)
    {
     Connection conn = ChessDatabase.getConnection();
      
          //Try update first
        try (PreparedStatement update = conn.prepareStatement(
                  "UPDATE players SET wins=?, losses=? WHERE name=?"
          );
             PreparedStatement insert = conn.prepareStatement(
              "INSERT INTO players(name, wins, losses) VALUES (?,?,?)"
              )) {
            for (Player p : players.values()) {
                update.setInt(1, p.getWins());
                update.setInt(2, p.getLosses());
                update.setString(3, p.getName());
                int n = update.executeUpdate();
                if (n == 0) {
                    insert.setString(1, p.getName());
                    insert.setInt(2, p.getWins());
                    insert.setInt(3, p.getLosses());
                    insert.executeUpdate();
          }
          }
      } catch (SQLException e)
      {
          throw new RuntimeException(e);
      }
    }
    
  /**
     * Increments the 'wins' count for a specified player in the database.
     * Uses a single UPDATE statement for efficiency.
     * @param name The name of the player whose wins should be incremented.
     */
    public static void updateWins(String name)
    {
        Connection conn = ChessDatabase.getConnection();
        
        // SQL to directly increment the 'wins' column by 1 for the player
        String sql = "UPDATE players SET wins = wins + 1 WHERE name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            
            ps.setString(1, name);
       
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected == 0) {
               
                System.out.println("Warning: Player '" + name + "' not found to update wins.");
            }

        } catch (SQLException e)
        {
            // Wrap the SQL exception in a runtime exception for consistent error handling
            throw new RuntimeException("Failed to update wins for player: " + name, e);
        }
    }
    
    // You should also consider adding a corresponding updateLosses(String name) method:
    /**
     * Increments the 'losses' count for a specified player in the database.
     * @param name The name of the player whose losses should be incremented.
     */
     public static void updateLosses(String name)
    {
        Connection conn = ChessDatabase.getConnection();
        String sql = "UPDATE players SET losses = losses + 1 WHERE name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException("Failed to update losses for player: " + name, e);
        }
    }
}

