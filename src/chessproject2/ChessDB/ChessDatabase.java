package chessproject2.ChessDB;

import java.sql.*;

/**
 * Handles connection and setup of the embedded Derby database Auto-creates
 * tables for players, games, and moves on the first run And also checks if the
 * tables have already been created
 *
 * @author RyanL and Yaacoub
 */
public class ChessDatabase {

    private static final String DB_URL = "jdbc:derby:chessdb;create=true";
    private static Connection conn;

    private ChessDatabase() {
    }

    public static synchronized void init() //The database is embedded so all we have to do is call ChessDatabase.init() when the app starts
    {
        getConnection();
    }

    private static void createTables(Connection c) throws SQLException //All of the table creation is stored here under the createTables method
    {

        execDDLIgnore(c,
                "CREATE TABLE games ("
                + "  game_name    VARCHAR(200) NOT NULL,"
                + "  white_player VARCHAR(200),"
                + "  black_player VARCHAR(200),"
                + "  turn         INT,"
                + "  board_state  CLOB,"
                + "  created_at   TIMESTAMP,"
                + "  CONSTRAINT pk_games PRIMARY KEY (game_name)"
                + ")"
        );

        execDDLIgnore(c,
                "CREATE TABLE moves ("
                + "  game_name   VARCHAR(200) NOT NULL,"
                + "  move_number INT NOT NULL,"
                + "  move_text   VARCHAR(400),"
                + "  CONSTRAINT pk_moves PRIMARY KEY (game_name, move_number)"
                + ")"
        );

        execDDLIgnore(c,
                "CREATE TABLE players ("
                + "  name   VARCHAR(200) NOT NULL,"
                + "  wins   INT,"
                + "  losses INT,"
                + "  CONSTRAINT pk_players PRIMARY KEY (name)"
                + ")"
        );

        execDDLIgnore(c, "ALTER TABLE games ADD COLUMN created_at TIMESTAMP");
    }

    private static void execDDLIgnore(Connection c, String sql) { //Just a method for creating a statement and executing it in SQL
        try (Statement st = c.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException ignored) {

        }
    }

    private static boolean tableAlreadyExists(SQLException e) {
        return e.getSQLState().equals("X0Y32"); //Derby code for Table already exists
    }

    public static Connection getConnection() //Helper method to allow us to connect to the database
    {
        try {
            if (conn == null || conn.isClosed()) {
                try {
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); //Call for the embedded derby database
                } catch (ClassNotFoundException ignore) {
                }
                conn = DriverManager.getConnection(DB_URL);
                createTables(conn);
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("DB connection failed", e);
        }
    }

    public static synchronized void close() //Synchronized close so that we prevent race conditions since multiple classes attempt to access the DB at the same time
    {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
        }
        conn = null;
    }
}
