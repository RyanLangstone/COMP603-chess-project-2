/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2.ChessDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Explosive
 */
public class SaveGameDB {
    //updates game row by name
    public static void saveOrUpdateGame(String gameName, String white, String black, int turn, String boardState)
    {
        try (Connection conn = ChessDatabase.getConnection())
        {
            int n;
            PreparedStatement upd = conn.prepareStatement("""
                       UPDATE games SET white_player=?, black_player=?, board_state=?, turn=? WHERE game_name=?
            """);
           
            upd.setString(1, white);
            upd.setString(2, black);
            upd.setInt(3, turn); 
            upd.setString(4, boardState);
            upd.setString(5, gameName);
            n =  upd.executeUpdate();
         if(n == 0)
         {
             PreparedStatement ins = conn.prepareStatement("""                              
             INSERT INTO games(game_name, white_player, black_player, turn, board_state) VALUES (?, ?, ?, ?, ?)
              """);
             ins.setString(1, gameName);
             ins.setString(2, white);
             ins.setString(3, black);
             ins.setInt(4, turn);
             ins.setString(5, boardState);
             ins.executeUpdate();
         }
    } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void updateTurnAndBoard(String gameName, int turn, String boardState)
    {
        try (Connection conn = ChessDatabase.getConnection())
        {
            PreparedStatement ps = conn.prepareStatement("""
                           UPDATE games SET turn=?, board_state=? WHERE game_name=?
             """);
            ps.setInt(1, turn);
            ps.setString(2, boardState);
            ps.setString(3, gameName);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
     public static void saveMove(String gameName, int moveNumber, String moveText)
    {
        try (Connection conn = ChessDatabase.getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO moves(game_name, move_number, move_text) VALUES (?,?,?)"
            );
            ps.setString(1, gameName);
            ps.setInt(2, moveNumber);
            ps.setString(3, moveText);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
     
     public static List<String> listGameNames()
     {
         List<String> out = new ArrayList<>();
         try(Connection conn = ChessDatabase.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT game_name FROM games ORDER BY created_at DESC");
                 ResultSet rs = ps.executeQuery())
         {
             while(rs.next()) out.add(rs.getString(1));
         } catch (SQLException e)
         {
             e.printStackTrace();
         }
         return out;
     }
     
      public static void printGameLog(String gameName)
    {
        try (Connection conn = ChessDatabase.getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(
            "SELECT move_number, move_text FROM moves WHERE game_name = ? ORDER BY move_number"
            );
            ps.setString(1, gameName);
           try ( ResultSet rs = ps.executeQuery())
           {
            while(rs.next())
            {
                System.out.println(rs.getInt("move_number") + ": " + rs.getString("move_text"));
            }
           }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
