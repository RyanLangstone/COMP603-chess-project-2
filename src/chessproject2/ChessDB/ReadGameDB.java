
package chessproject2.ChessDB;


import chessproject2.Check;
import chessproject2.Pieces.PieceFactory;
import chessproject2.Pieces.Piece;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/** Class responsible for reading a saved chess board from a text file
 * and loading it into the current game state 
 *
 * Expected file format:
 * Metadata lines start with # such as #GameName, #WhitePlayer, #BlackPlayer, #Turn
 * -8 lines, each representing a row of the chess board
 * -Each square is written as (PieceType, isWhite) or (null)
 * -Example row: (Rook, true) (Knight, true) (null)
 * 
 * This class reconstructs the board state by parsing the file line by line
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
        
        if (!rs.next())
        {
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
        System.out.println("White: " +Check.whiteName + " | Black: " + Check.blackName + " | Turn: " + Check.turn);
        return true;
       }
    } catch (SQLException e)
    {
        e.printStackTrace();
        return false;
    }
}
    
    /*
    Rebuilds the Check.board from the board_state string
    Format Example: (Pawn, true)(null)(Rook,false)
    Row-separated by newlines or semicolons
    */
    private static void parseBoardState(String boardState)
    {
        if(boardState == null || boardState.isEmpty()) return;
        
        //Reset board
        Check.board = new Piece[8][8];
        
        //Split by newlines or semicolons to separate rows
        String[] rows = boardState.trim().split("[\\n;]");
        for(int row = 0; row < rows.length && row < 8; row++)
        {
            String line = rows[row].replace(")(", ") ("); //add space to separate pieces
            String[] tokens = line.trim().split("\\s+");
            
            for (int col = 0; col < tokens.length && col < 8; col++)
            {
                String token = tokens[col].trim();
                if(token.equals("(null)"))
                {
                    Check.board[row][col] = null;
                } else {
                    token = token.substring(1, token.length() - 1); //remove brackets
                    String[] parts = token.split(",");
                    String type = parts[0];
                    boolean isWhite = Boolean.parseBoolean(parts[1]);
                    Check.board[row][col] = PieceFactory.fromType(type, isWhite, row, col);
                }
            }
        }
    }
    
    public static List<String> loadMoves(String gameName)
    {
        Connection conn = ChessDatabase.getConnection();
         List<String> moves = new ArrayList<>();
          try (PreparedStatement ps = conn.prepareStatement(
            "SELECT move_text FROM moves WHERE game_name = ? ORDER BY move_number"
            )) {
            ps.setString(1, gameName);
           try (ResultSet rs = ps.executeQuery()) {
            
            while(rs.next())
            {
                moves.add(rs.getString("move_text"));
            }
           }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return moves;
    }
}
