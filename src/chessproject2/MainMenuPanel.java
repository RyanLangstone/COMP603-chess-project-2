/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Explosive
 */
public class MainMenuPanel extends JPanel {
    public MainMenuPanel (Game game)
    {
        setLayout(new GridLayout(7, 1, 10, 10));
        setBackground(new Color(30, 30, 30));
        
        JLabel title = new JLabel("Chess Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        
        JButton btnInstructions = new JButton("1. View Instructions");
        JButton btnLoad = new JButton("2. Load Saved Game");
        JButton btnNewGame = new JButton("3. Start New Game");
        JButton btnStats = new JButton("4. View Player Stats");
        JButton btnLogs = new JButton("5. View Game Logs");
        JButton btnExit = new JButton("Exit");
        
        btnInstructions.addActionListener(e -> game.showPanel("Instructions"));
        btnLoad.addActionListener(e -> {
            String file = SaveGameLoader.chooseSaveFileGUI();
            if (file != null)
            {
                ReadBoardFileTest.ReadBoardFile(file);
                game.showPanel("Game");
            }
        });
        
        btnNewGame.addActionListener(e -> game.showPanel("PlayerSetup"));
        btnStats.addActionListener(e -> game.showPanel("Stats"));
        btnLogs.addActionListener(e -> game.showPanel("Logs"));
        btnExit.addActionListener(e -> System.exit(0));
        
        add(title);
        add(btnInstructions);
        add(btnLoad);
        add(btnNewGame);
        add(btnStats);
        add(btnLogs);
        add(btnExit);
    }
}
