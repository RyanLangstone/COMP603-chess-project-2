/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Explosive
 */
public class PlayerSetupPanel extends JPanel {
    private JTextField gameNameField, whiteField, blackField;
    private JButton startBtn, backBtn;
    
    public PlayerSetupPanel(Game game)
    {
        setLayout(new GridLayout(6, 2, 10, 10));
        JLabel lblGame = new JLabel("Game Name: ");
        JLabel lblWhite = new JLabel("White Player: ");
        JLabel lblBlack = new JLabel("Black Player: ");
        
        gameNameField = new JTextField();
        whiteField = new JTextField();
        blackField = new JTextField();
        
        startBtn = new JButton("Start Game");
        backBtn = new JButton("Back to Menu");
        
        startBtn.addActionListener(e -> {
            String g = gameNameField.getText();
            String w = whiteField.getText();
            String b = blackField.getText();
            
            if (g.isEmpty() || w.isEmpty() || b.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            
            //Save Players
            HashMap<String, Player> players = PlayerFileIO.loadPlayers();
            players.putIfAbsent(w, new Player(w));
            players.putIfAbsent(b, new Player(b));
            
            game.startNewGame(g, w, b);
        });
        
        backBtn.addActionListener(e -> game.showPanel("Menu"));
        
        add(lblGame);
        add(gameNameField);
        add(lblWhite);
        add(whiteField);
        add(lblBlack);
        add(blackField);
        add(startBtn);
        add(backBtn);
    }
    
}
