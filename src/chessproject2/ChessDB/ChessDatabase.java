/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2.ChessDB;

import java.sql.*;

/**
 * Handles connection and setup of the embedded Derby database
 * Auto-creates tables for players, games, and moves on the first run
 * @author Explosive
 */
public class ChessDatabase {
    private static final String DB_URL = "jdbc:derby:chessdb;create=true";
    private static Connection conn;
    
    public static void init() //The database is embedded so all we have to do is call ChessDatabase.init() when the app starts
    {
        try{
            conn = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Connected successfully to the embedded Derby database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void createTables() throws SQLException
    {
        Statement stmt = conn.createStatement();
        
        //Player table
        try {
        stmt.executeUpdate (
                "CREATE TABLE players (" +
                 "name VARCHAR(50) PRIMARY KEY, " +
                 "wins INT DEFAULT 0, " +
                 "losses INT DEFAULT 0)"
        );
            System.out.println("Created table: players");
        } catch (SQLException e)
        {
            if (!tableAlreadyExists(e)) throw e;
        }
        
        //Game table
        try {
        stmt.executeUpdate(
                "CREATE TABLE games (" +
                "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "game_name VARCHAR(50), " +
                 "white_player VARCHAR(50), " +
                 "black_player VARCHAR(50), " +
                        "turn INT, " +
                        "board_state CLOB, " + //Stores the entire board as text
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        );
        
        System.out.println("Tables created successfully");
        } catch (SQLException e)
        {
            if (!tableAlreadyExists(e)) throw e;
        }
        
        try {
            stmt.executeUpdate(
            "CREATE TABLE moves (" +
                    "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "game_name VARCHAR(50), " +
                    "move_number INT, " +
                    "move_text VARCHAR(255))"
            );
            System.out.println("Created table: moves");
        } catch (SQLException e)
        {
            if (!tableAlreadyExists(e)) throw e;
        }
        
        stmt.close();
    }
    
    private static boolean tableAlreadyExists(SQLException e)
    {
        return e.getSQLState().equals("X0Y32"); //Derby code for Table already exists
    }
    
    public static Connection getConnection()
    {
        return conn;
    }
    
    public static void close()
    {
        try
        {
            if (conn != null) conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
