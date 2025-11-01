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
        Connection conn = ChessDatabase.getConnection();
       try {
           try (PreparedStatement upd = conn.prepareStatement("""
                       UPDATE games SET white_player=?, black_player=?, board_state=?, turn=? WHERE game_name=?
            """)) {
           
            upd.setString(1, white);
            upd.setString(2, black);
            upd.setString(3, boardState); 
            upd.setInt(4, turn);
            upd.setString(5, gameName);
            int n =  upd.executeUpdate();
           
         if(n == 0)
         {
           try  (PreparedStatement ins = conn.prepareStatement("""                              
             INSERT INTO games(game_name, white_player, black_player, turn, board_state, created_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
              """)) {
             ins.setString(1, gameName);
             ins.setString(2, white);
             ins.setString(3, black);
             ins.setInt(4, turn);
             ins.setString(5, boardState);
             ins.executeUpdate();
           }
         }
        }
    } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void updateTurnAndBoard(String gameName, int turn, String boardState)
    {
       Connection conn = ChessDatabase.getConnection();
        
          try (PreparedStatement ps = conn.prepareStatement("""
                           UPDATE games SET turn=?, board_state=? WHERE game_name=?
             """)) {
            ps.setInt(1, turn);
            ps.setString(2, boardState);
            ps.setString(3, gameName);
            ps.executeUpdate();
        } catch (SQLException e)
        {
           throw new RuntimeException(e);
        }
    }

     public static void saveMove(String gameName, int moveNumber, String moveText)
    {
        Connection conn = ChessDatabase.getConnection();
        {
         try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO moves(game_name, move_number, move_text) VALUES (?,?,?)"
            )) {
            ps.setString(1, gameName);
            ps.setInt(2, moveNumber);
            ps.setString(3, moveText);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    }
     public static List<String> listGameNames()
     {
         Connection conn = ChessDatabase.getConnection();
         List<String> out = new ArrayList<>();
         try (PreparedStatement ps = conn.prepareStatement("SELECT game_name FROM games ORDER BY created_at DESC, game_name ASC");
                 ResultSet rs = ps.executeQuery()) {
       
             while(rs.next()) out.add(rs.getString(1));
         } catch (SQLException e)
         {
            throw new RuntimeException(e);
         }
         return out;
     }
     
     public static List<String> loadMoves(String gameName)
     {
         Connection conn = ChessDatabase.getConnection();
         List<String> moves = new ArrayList<>();
         try(PreparedStatement ps = conn.prepareStatement(
         "SELECT move_number, move_text FROM moves WHERE game_name=? ORDER BY move_number"
         )) {
             ps.setString(1, gameName);
             try (ResultSet rs = ps.executeQuery())
             {
                 while (rs.next())
                 {
                     moves.add(rs.getInt(1) + ": " + rs.getString(2));
                 }
             }
         } catch (SQLException e)
         {
             throw new RuntimeException(e);
         }
         return moves;
     }
}
