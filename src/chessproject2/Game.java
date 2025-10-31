package chessproject2;

import chessproject2.Pieces.Piece;
import chessproject2.Pieces.PieceConversion;
import chessproject2.Pieces.PieceFactory;
import static chessproject2.Pieces.PieceConversion.chessNotationToIndex;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Main game logic for chess Handles initialization, menus, players, board
 * rendering, move validation, and special rules
 *
 * @author RyanL and Yaacoub
 */
public class Game {

    //8x8 chess board, stores Piece objects or nul
    public static Piece[][] board = new Piece[8][8];

    //Stores unicode box drawing characters used to render the chess board
    static HashMap<String, String> chars = new HashMap();

    static {
        // Box-drawing characters
        chars.put("TL", "\u250C"); // ┌
        chars.put("TR", "\u2510"); // ┐
        chars.put("BL", "\u2514"); // └
        chars.put("BR", "\u2518"); // ┘
        chars.put("H", "\u2500");  // ─
        chars.put("V", "\u2502");  // │
        chars.put("TM", "\u252C"); // ┬
        chars.put("BM", "\u2534"); // ┴
        chars.put("LM", "\u251C"); // ├
        chars.put("RM", "\u2524"); // ┤
        chars.put("MM", "\u253C"); // ┼
    }

    //Current game metadata
    public static String gameName = ""; //Current game name
    public static String whiteName = ""; // White players name
    public static String blackName = ""; //Black players name
    public static int turn = 0; // Current turn counter (even = white, odd = black)

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        //Ensures console supports UTF-8 so unicode chess pieces render in correctly
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        //Loads existing players from PlayerFileIO
        HashMap<String, Player> players = PlayerFileIO.loadPlayers();

        //Writes and loads the default starting chess board
        BoardFileIO.writeDefaultBoard();
        BoardFileIO.loadDefaultBoard();

        //Gets ASCII/Unicode border strings for drawing the board
        String[] borderString = GetBorder.getborder();

        //Scanner for user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to our Chess game");

        // Main Menu Loop
        int option;

        mainMenuLoop:

        while (true) {
            System.out.println("Type the number to select the next step:");
            System.out.println("(1): View instructions");
            System.out.println("(2): Load Save files");
            System.out.println("(3): Start new game");
            System.out.println("(4): View Players Stats");
            System.out.println("(5): View Game Logs");
            String input = scanner.next();
            if (input.equalsIgnoreCase("x")) {
                System.out.println("Exiting");
                scanner.close();
                return;
            }
            try {
                option = Integer.parseInt(input);
                if (option == 1) { //Shows you the instructions then loops back to the top for the next number selection
                    System.out.println("Here are the instructions:");
                    System.out.println("(1) Select a save game file or start a new game");
                    System.out.println("(2) If you choose to start a new game, You need to choose a game name");
                    System.out.println("(3) When choosing black or white players, you have the option to create a new player or choose an existing player");
                    System.out.println("(4) When the game begins, you can choose what piece you want to move and where to move it (it is shown by the green squares)");
                    System.out.println("(5) Your goal is to checkmate the other colors king. First one to checkmate wins!");
                    System.out.println("Good luck!!!");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                } else if (option == 2) {
                    //Loads a saved game/board
                    System.out.println("Loading save board");
                    String savePath = SaveGameLoader.chooseSaveFile(scanner);
                    if (savePath == null || savePath.equals("BACK")) {
                        continue mainMenuLoop; //Returns to the top if there are no saved games
                    }
                    ReadBoardFileTest.ReadBoardFile(savePath);
                    break; //starts the game from the loaded save file
                } else if (option == 3) {
                    //Starts a new game
                    System.out.println("Creating new game:");
                    break;
                } else if (option == 4) { //Shows the list of players and stats (wins + losses)
                    System.out.println("Players list: ");
                    int i = 1;
                    for (Player p : players.values()) {
                        System.out.println("(" + i + ") " + p.getName() + " (Wins: " + p.getWins() + ",  Losses: " + p.getLosses() + ")");
                        i++;
                    }
                    continue mainMenuLoop;
                } else if (option == 5) { //Loads and shows all moves for a certain game
                    System.out.println("Loading game logs...");
                    String logFile = MoveLogLoader.chooseLogFile(scanner);
                    if (logFile == null || logFile.equals("BACK")) {
                        continue mainMenuLoop;
                    }
                    System.out.println("Moves for this game: ");
                    List<String> moves = PlayerMovesFileIO.loadMoves(logFile);
                    if (moves.isEmpty()) {
                        System.out.println("No moves recorded yet.");
                    } else {
                        for (String m : moves) {
                            System.out.println(m);
                        }
                    }
                    continue mainMenuLoop;
                } else {
                    System.out.println("Select a valid number");
                }
            } catch (Exception e) {
                System.out.println("Intiger input needed");

            }
        }
        //New Game selection
        if (option == 3) {
            //select load existing player or new player and allows player to choose game name
            System.out.println("Type game name:");
            gameName = scanner.next();
            System.out.println(gameName);
            //White player selection
            System.out.println("Select white player");
            System.out.println("(1): existing player");
            System.out.println("(2): new player");
            while (true) {
                String input = scanner.next();
                if (input.equalsIgnoreCase("x")) {
                    System.out.println("Exiting");
                    scanner.close();
                    System.exit(0);
                } else {
                    try {
                        int selected = Integer.parseInt(input);
                        if (selected == 1) {
                            System.out.println("Retrieving list of players: ...");
                            if (players.isEmpty()) {
                                System.out.println("No players found. Please create a new one.");
                                System.out.print("Enter player name: ");
                                whiteName = scanner.next();
                                players.put(whiteName, new Player("White: " + whiteName));
                                break;
                            } else {
                                int i = 1;
                                for (Player p : players.values()) {
                                    System.out.println("(" + i + ") " + p.getName() + " (Wins: " + p.getWins() + ",  Losses: " + p.getLosses() + ")");
                                    i++;
                                }

                                int choice;
                                System.out.println("Select a player number");
                                String input2 = scanner.next();
                                while (true) {
                                    try {
                                        if (input2.equalsIgnoreCase("x")) {
                                            System.out.println("Exiting");
                                            scanner.close();
                                            System.exit(0);
                                        }

                                        choice = Integer.parseInt(input2);
                                        if (choice > 0 && choice <= players.size()) {
                                            whiteName = (String) players.keySet().toArray()[choice - 1];
                                            break;
                                        } else {
                                            System.out.println("Invalid choice.");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Please enter a number.");
                                        scanner.close();
                                    }
                                }
                                break;
                            }
                            //New player creation (White)
                        } else if (selected == 2) {
                            System.out.println("Enter player name:");
                            whiteName = scanner.next();
                            players.put(whiteName, new Player("White: " + whiteName));
                            if (whiteName.equalsIgnoreCase("x")) {
                                System.out.println("Exiting");
                                scanner.close();
                                return;
                            }
                            break;
                        } else {
                            System.out.println("Select a valid number");
                        }
                    } catch (Exception e) {
                        System.out.println("Intiger input needed");
                        scanner.nextLine();
                    }
                }

            }
            //Black player selection/creation
            System.out.println("Select black player");
            System.out.println("(1): existing player");
            System.out.println("(2): new player");
            while (true) {
                String input = scanner.next();
                if (input.equalsIgnoreCase("x")) {
                    System.out.println("Exiting");
                    scanner.close();
                    System.exit(0);
                }

                try {
                    int selected = Integer.parseInt(input);
                    if (selected == 1) {
                        System.out.println("Retrieving list of players: ...");
                        if (players.isEmpty()) {
                            System.out.println("No players found. Please create a new one.");
                            System.out.print("Enter player name: ");
                            String name = scanner.next();
                            players.put(blackName, new Player("Black: " + blackName));
                            break;
                        } else {
                            int i = 1;
                            for (Player p : players.values()) {
                                System.out.println("(" + i + ") " + p.getName() + " (Wins: " + p.getWins() + ", Losses: " + p.getLosses() + ")");
                                i++;
                            }

                            int choice;
                            System.out.println("Select a player number");
                            String input2 = scanner.next();
                            while (true) {
                                try {
                                    if (input2.equalsIgnoreCase("x")) {
                                        System.out.println("Exiting");
                                        scanner.close();
                                        System.exit(0);
                                    }

                                    choice = Integer.parseInt(input2);
                                    if (choice > 0 && choice <= players.size()) {
                                        blackName = (String) players.keySet().toArray()[choice - 1];
                                        break;
                                    } else {
                                        System.out.println("Invalid choice.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Please enter a number.");
                                    scanner.close();
                                }
                            }
                            break;
                        }
                    } else if (selected == 2) {

                        System.out.println("Enter player name:");
                        blackName = scanner.next();
                        players.put(blackName, new Player("Black: " + blackName));
                        if (blackName.equalsIgnoreCase("x")) {
                            System.out.println("Exiting");
                            scanner.close();
                            return;
                        }
                        break;
                    } else {
                        System.out.println("Select a valid number");
                    }
                } catch (Exception e) {
                    System.out.println("Intiger input needed");
                    scanner.nextLine();
                }

            }
            //Saves players after creation
            PlayerFileIO.savePlayers(players);
        }

        //Gameplay loop
        System.out.println("Turn:" + turn);
        while (true) {
            //prints current board state
            System.out.println(gameName);
            for (int row = 8; row > 0; row--) {
                System.out.println(borderString[row]); //Horizontal border
                for (int column = 0; column < 8; column++) {
                    if (board[row - 1][column] != null) {
                        //Prints piece
                        System.out.print(chars.get("V") + "\u2004" + board[row - 1][column].unicode + "\u202F");
                    } else {
                        //Empty square/Null
                        System.out.print(chars.get("V") + "\u205F  \u205F");
                    }
                    if (column == 7) {
                    }
                }
                //Prints row number at the end
                System.out.println(chars.get("V") + " " + row);
            }
            //Bottom border and column labels
            System.out.println(borderString[0]);
            System.out.println("  a\u205F\u200A  b\u205F\u200A  c\u205F\u200A  d\u205F\u200A  e\u205F\u200A  f\u205F\u200A  g\u205F\u200A  h");
            System.out.println();

            //Tracks whose turn it is
            boolean whiteTurn;
            if (turn % 2 == 0) {
                System.out.println(whiteName + "'s turn");
                whiteTurn = true;
            } else {
                System.out.println(blackName + "'s turn");
                whiteTurn = false;
            }

            //Piece selection loop
            int[] from;
            Piece piece;
            int[][] validMoves;
            while (true) {
                System.out.println("Enter piece to move: (e.g e2) ");
                String input = scanner.next();
                if (input.equalsIgnoreCase("x")) {
                    //Saves and exits
                    if (turn == 0) {
                        System.out.println("Are you sure? No moves have been made yet (y/n).");
                        String confirm = scanner.next();
                        if (confirm.equalsIgnoreCase("n")) {
                            continue;
                        }
                    }
                    System.out.println("Exiting");
                    String saveFileName = BoardFileWrite.getNextSaveFileName();
                    BoardFileWrite.WriteBoardFile(saveFileName);
                    System.out.println("Game saved as " + saveFileName);
                    scanner.close();
                    return;
                }

                //Convers chess notation to indices
                from = chessNotationToIndex(input);
                if (from == null) {
                    System.out.println("Invalid input. Use a1 to h8");
                    continue;
                }
                //Validate range
                if (from[0] < 0 || from[0] > 7 || from[1] < 0 || from[1] > 7) {
                    System.out.println("Out of bounds. Use a1 to h8");
                    continue;
                }
                //Must have a piece at that square
                if (board[from[0]][from[1]] == null) {
                    System.out.println("No piece at that sqaure. Try again.");
                    continue;
                }
                //Must be players own color and not opponents
                if ((board[from[0]][from[1]].isWhite == true && !whiteTurn) || (board[from[0]][from[1]].isWhite == false && whiteTurn)) {
                    System.out.println("Oponents Piece selected. Try again.");
                    continue;
                }
                //Valid piece selected
                piece = board[from[0]][from[1]];
                validMoves = piece.ValidMoves(board, true);
                if (validMoves.length == 0) {
                    System.out.println("That " + piece.type + " has no valid moves. Choose another");
                    continue;
                }
                break;
            }
            //Prints board highlighting valid moves (Green boxes)
            for (int row = 8; row > 0; row--) {
                System.out.println(borderString[row]);
                moves:
                for (int column = 0; column < 8; column++) {
                    for (int[] validMove : validMoves) {
                        if (validMove[0] == row - 1 && validMove[1] == column) {
                            //Highlights valid move squares
                            if (board[row - 1][column] != null) {
                                //Capture target highlighted red
                                System.out.print(chars.get("V") + "\u001B[41m\u2004" + board[row - 1][column].unicode + "\u202F\u001B[0m");
                            } else {
                                //Empty square highlighted green
                                System.out.print(chars.get("V") + "\u001B[42m\u205F  \u205F\u001B[0m");
                            }
                            continue moves;
                        }
                    }
                    //Otherwise prints out normally
                    if (board[row - 1][column] != null) {
                        System.out.print(chars.get("V") + "\u2004" + board[row - 1][column].unicode + "\u202F");
                    } else {
                        System.out.print(chars.get("V") + "\u205F  \u205F");
                    }

                }
                System.out.println(chars.get("V") + " " + row);
            }
            System.out.println(borderString[0]);
            System.out.println("  a\u205F\u200A  b\u205F\u200A  c\u205F\u200A  d\u205F\u200A  e\u205F\u200A  f\u205F\u200A  g\u205F\u200A  h");
            System.out.println("");

            //Move input loop
            int[] to;
            while (true) {
                System.out.println("Enter new piece location: (e.g e2)");
                String input = scanner.next();
                if (input.equalsIgnoreCase("x")) {
                    if (turn == 0) {
                        System.out.println("Are you sure? No moves have been made yet (y/n).");
                        String confirm = scanner.next();
                        if (confirm.equalsIgnoreCase("n")) {
                            continue;
                        }
                    }
                    System.out.println("Exiting");
                    String saveFileName = BoardFileWrite.getNextSaveFileName();
                    BoardFileWrite.WriteBoardFile(saveFileName);
                    System.out.println("Game saved as " + saveFileName);
                    scanner.close();
                    return;
                }

                //Converts input to board indices
                to = chessNotationToIndex(input);
                if (to == null || to[0] < 0 || to[0] > 7 || to[1] < 0 || to[1] > 7) {
                    System.out.println("Invalid input. Use a1 to h8");
                    continue;
                }

                //Check if the move is valid
                boolean isValid = false;

                for (int[] move : validMoves) {
                    if (move[0] == to[0] && move[1] == to[1]) {
                        isValid = true;
                        break;
                    }
                }

                if (!isValid) {
                    System.out.println("Invalid move for " + piece.type + " . Try again.");
                    continue; //asks for another move
                }

                //Special rules
                // checks En Passant
                if (piece.type == "Pawn" && to[1] != from[1] && board[to[0]][to[1]] == null) {
                    board[from[0]][to[1]] = null; //capture the En Passant piece
                }

                //If its valid, it makes the move
                board[to[0]][to[1]] = piece;
                board[to[0]][to[1]].setPosition(to[0], to[1]);
                board[to[0]][to[1]].turnMoved = turn;
                board[from[0]][from[1]] = null;

                //Save the move in chess notation
                String moveNotation = (piece.isWhite ? "W:  " : "B:  ")
                        + PieceConversion.indexToChessNotation(from) + " -> "
                        + PieceConversion.indexToChessNotation(to);
                PlayerMovesFileIO.saveMove(gameName, moveNotation);

                //checks castling
                if (piece.type == "King" && to[1] - from[1] == 2) {
                    board[to[0]][to[1] - 1] = board[to[0]][7];
                    board[to[0]][to[1] - 1].setPosition(to[0], to[1 + 1]);
                    board[to[0]][7] = null;
                } else if (piece.type == "King" && to[1] - from[1] == -2) {
                    board[to[0]][to[1] + 1] = board[to[0]][0];
                    board[to[0]][to[1] + 1].setPosition(to[0], to[1 - 1]);
                    board[to[0]][0] = null;
                }

                // checks for pawn promotion
                if (board[to[0]][to[1]].type == "Pawn" && ((whiteTurn && to[0] == 7) || (!whiteTurn && to[0] == 0))) {
                    System.out.println("Promote your pawn");
                    System.out.println("type the number to select promotion:");
                    System.out.println("(1)Rook");
                    System.out.println("(2)Knight");
                    System.out.println("(3)Bishop");
                    System.out.println("(4)Queen");
                    while (true) {
                        String promotion = scanner.next();
                        if (promotion.equalsIgnoreCase("x")) {
                            System.out.println("Exiting");
                            scanner.close();
                            return;
                        } else {
                            try {
                                int selected = Integer.parseInt(promotion);
                                if (selected == 1) {
                                    System.out.println("Rook Selected");
                                    board[to[0]][to[1]] = PieceFactory.fromType("Rook", whiteTurn, to[0], to[1]);
                                    break;
                                } else if (selected == 2) {
                                    System.out.println("Knight Selected");
                                    board[to[0]][to[1]] = PieceFactory.fromType("Knight", whiteTurn, to[0], to[1]);

                                    break;
                                } else if (selected == 3) {
                                    System.out.println("Bishop Selected");
                                    board[to[0]][to[1]] = PieceFactory.fromType("Bishop", whiteTurn, to[0], to[1]);

                                    break;
                                } else if (selected == 4) {
                                    System.out.println("Queen Selected");
                                    board[to[0]][to[1]] = PieceFactory.fromType("Queen", whiteTurn, to[0], to[1]);

                                    break;
                                } else {
                                    System.out.println("Select a valid number");
                                }
                            } catch (Exception e) {
                                System.out.println("Intiger input needed");
                                scanner.nextLine();
                            }
                        }
                    }
                }

                // === CHECK / CHECKMATE HANDLING ===
                boolean opponentIsWhite = !piece.isWhite;
                if (isCheckmate(opponentIsWhite, board)) { // <-- ADDED
                    String winnerName = piece.isWhite ? whiteName : blackName;
                    String loserName = piece.isWhite ? blackName : whiteName;
                    System.out.println("CHECKMATE! " + winnerName + " wins!");
                    if (players.containsKey(winnerName)) {
                        players.get(winnerName).addWin();
                    }
                    if (players.containsKey(loserName)) {
                        players.get(loserName).addLosses();
                    }
                    PlayerFileIO.savePlayers(players);
                    break;
                    //add stuff

                } else {
                    // Just check
                    // Find opponent king
                    int kingRow = -1, kingCol = -1;
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            Piece p = board[r][c];
                            if (p != null && p.type.equals("King") && p.isWhite == opponentIsWhite) {
                                kingRow = r;
                                kingCol = c;
                            }
                        }
                    }
                    if (kingRow != -1 && isSquareAttacked(kingRow, kingCol, piece.isWhite, board)) {
                        System.out.println("CHECK!");
                    }
                }

                //setup for next turn
                turn++;
                System.out.println("Turn:" + turn);
                break;
            }
        }
    }

    public static boolean isCheckmate(boolean isWhiteTurn, Piece[][] board) {
        // 1. Locate the king
        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.type.equals("King") && p.isWhite == isWhiteTurn) {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        if (kingRow == -1) {
            // no king found (shouldn’t happen)
            return true;
        }

        // 2. Check if king is under attack
        if (!isSquareAttacked(kingRow, kingCol, !isWhiteTurn, board)) {
            return false; // not in check → no checkmate
        }

        // 3. Try all king moves
        int[][] kingMoves = board[kingRow][kingCol].ValidMoves(board, true);
        for (int[] move : kingMoves) {
            int newRow = move[0], newCol = move[1];
            Piece backup = board[newRow][newCol];
            board[newRow][newCol] = board[kingRow][kingCol];
            board[kingRow][kingCol] = null;

            boolean stillInCheck = isSquareAttacked(newRow, newCol, !isWhiteTurn, board);

            // undo move
            board[kingRow][kingCol] = board[newRow][newCol];
            board[newRow][newCol] = backup;

            if (!stillInCheck) {
                return false; // king can escape
            }
        }

        // 4. Try all moves from friendly pieces
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null || p.isWhite != isWhiteTurn || p.type.equals("King")) {
                    continue;
                }

                int[][] moves = p.ValidMoves(board, true);
                for (int[] move : moves) {
                    int newRow = move[0], newCol = move[1];
                    Piece backup = board[newRow][newCol];
                    board[newRow][newCol] = p;
                    board[r][c] = null;

                    // after move, is king still in check?
                    boolean stillInCheck = isSquareAttacked(kingRow, kingCol, !isWhiteTurn, board);

                    // undo move
                    board[r][c] = p;
                    board[newRow][newCol] = backup;

                    if (!stillInCheck) {
                        return false; // a block or capture worked
                    }
                }
            }
        }

        // 5. No escape, no block, no capture → checkmate
        return true;

    }

    /**
     * Utility to check if a square is attacked by enemy pieces
     */
    public static boolean isSquareAttacked(int row, int col, boolean byWhite, Piece[][] board) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null || p.isWhite != byWhite) {
                    continue;
                }

                int[][] moves = p.ValidMoves(board, false);
                for (int[] m : moves) {
                    if (m[0] == row && m[1] == col) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getTurn() {
        return turn;
    }

}
