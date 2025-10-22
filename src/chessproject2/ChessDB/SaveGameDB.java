/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2.ChessDB;

import java.sql.*;

/**
 *
 * @author Explosive
 */
public class SaveGameDB {
    public static void saveGame(String gameName, String white, String black, int turn, String boardState)
    {
        try (Connection conn = ChessDatabase.getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO games(game_name, white_player, black_player, turn, board_state) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setString(1, gameName);
            ps.setString(2, white);
            ps.setString(3, black);
            ps.setInt(4, turn);
            ps.setString(5, boardState);
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
    
    public static void printGameLog(String gameName)
    {
        try (Connection conn = ChessDatabase.getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(
            "SELECT move_number, move_text FROM moves WHERE game_name = ? ORDER BY move_number"
            );
            ps.setString(1, gameName);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                System.out.println(rs.getInt("move_number") + ": " + rs.getString("move_text"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
}
