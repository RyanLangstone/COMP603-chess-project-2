package chessproject2.ChessDB;

import chessproject2.Check;
import chessproject2.GUI.BoardPanel;
import chessproject2.Pieces.PieceFactory;
import chessproject2.Pieces.Piece;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ReadGameDB is used in the LoadGameFrame to allow the player a selection of
 * games that they have already played
 *
 * @author RyanL and Yaacoub
 *
 */
public class ReadGameDB {

    public static boolean loadGame(String gameName) {
        Connection conn = ChessDatabase.getConnection();
        //Retrieve game data
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT white_player, black_player, turn, board_state FROM games WHERE game_name = ?"
        )) {
            ps.setString(1, gameName);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("Game not found in database: " + gameName);
                    return false;
                }

                //Extract metadata
                Check.gameName = gameName;
                Check.whiteName = rs.getString("white_player");
                Check.blackName = rs.getString("black_player");
                Check.turn = rs.getInt("turn");
                parseBoardState(rs.getString("board_state"));
                System.out.println("Game loaded successfully from DB: " + gameName);
                System.out.println("White: " + Check.whiteName + " | Black: " + Check.blackName + " | Turn: " + Check.turn);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    Rebuilds the Check.board from the board_state string
    Format Example: (Pawn, true)(null)(Rook,false)
    Row-separated by newlines or semicolons
     */
    private static void parseBoardState(String boardState) {
        if (boardState == null || boardState.trim().isEmpty()) {
            BoardPanel.board = new Piece[8][8];
            return;
        }

        //Reset board
        BoardPanel.board = new Piece[8][8];

        //Split by newlines or semicolons to separate rows
        String[] rows = boardState.trim().split("[\\n;]");
        for (int row = 0; row < rows.length && row < 8; row++) {
            String line = rows[row].replace(")(", ") ("); //add space to separate pieces
            String[] tokens = line.trim().split("\\s+");

            for (int col = 0; col < tokens.length && col < 8; col++) {
                String token = tokens[col].trim();
                if (token.equals("(null)")) {
                    BoardPanel.board[row][col] = null;
                } else {
                    token = token.substring(1, token.length() - 1); //remove brackets
                    String[] parts = token.split(",");
                    String type = parts[0];
                    boolean isWhite = Boolean.parseBoolean(parts[1]);

                    // Create the piece
                    Piece p = PieceFactory.fromType(type, isWhite, row, col);

                    // Check if turnMoved data exists and load it
                    // This keeps it compatible with old saves that only had 2 parts
                    if (parts.length > 2) {
                        p.turnMoved = Integer.parseInt(parts[2]);
                    }

                    BoardPanel.board[row][col] = p;
                }
            }
        }
    }

    public static class GameMeta //Constructor to help with loading the game meta data
    {

        public final String white;
        public final String black;
        public final int turn;

        public GameMeta(String w, String b, int t) {
            this.white = w;
            this.black = b;
            this.turn = t;
        }
    }

    public static GameMeta fetchGameMeta(String gameName) //Used in ReadGameDB to fetch the gameName and the players names and turn
    {
        Connection conn = ChessDatabase.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("""
                                             SELECT white_player, black_player, turn FROM games WHERE game_name=?             
                                                          """
        )) {
            ps.setString(1, gameName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new GameMeta(rs.getString(1), rs.getString(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
