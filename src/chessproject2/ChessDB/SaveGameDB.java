package chessproject2.ChessDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SaveGameDB is used to update or save certain aspects of the game that can
 * later be reused by the GUI to show Players specific things that they want to
 * see
 *
 * @author RyanL and Yaacoub
 */
public class SaveGameDB {

    public static void saveOrUpdateGame(String gameName, String white, String black, int turn, String boardState) //Used in NewGameFrame to save the game and update it as it goes on
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
                int n = upd.executeUpdate();

                if (n == 0) {
                    try (PreparedStatement ins = conn.prepareStatement("""                              
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateTurnAndBoard(String gameName, int turn, String boardState) //Used in GameFrame to update turns and board state, it uses BoardStateCodec to save the board state
    {
        Connection conn = ChessDatabase.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("""
                           UPDATE games SET turn=?, board_state=? WHERE game_name=?
             """)) {
            ps.setInt(1, turn);
            ps.setString(2, boardState);
            ps.setString(3, gameName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMove(String gameName, int moveNumber, String moveText) //Used in GameFrame to save moves to the gameLog aswell as pawn promotion
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<String> listGameNames() //Used in both GameLog and also LoadGame to show past games to choose from for moves or to show the player the list of options for previous games
    {
        Connection conn = ChessDatabase.getConnection();
        List<String> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT game_name FROM games ORDER BY created_at DESC, game_name ASC"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    public static List<String> loadMoves(String gameName) //Used in gameLog to show players moves of previous games
    {
        Connection conn = ChessDatabase.getConnection();
        List<String> moves = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT move_number, move_text FROM moves WHERE game_name=? ORDER BY move_number"
        )) {
            ps.setString(1, gameName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    moves.add(rs.getInt(1) + ": " + rs.getString(2));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return moves;
    }
}
