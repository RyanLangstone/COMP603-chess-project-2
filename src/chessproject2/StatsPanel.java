/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Explosive
 */
public class StatsPanel extends JPanel {
    private JLabel totalMovesLabel;
    private JLabel whiteMovesLabel;
    private JLabel blackMovesLabel;
    private Game game;
    
    public StatsPanel(Game game)
    {
        this.game = game;
        
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createTitledBorder("Game Stats"));
        
        totalMovesLabel = new JLabel("Total Moves: 0");
        whiteMovesLabel = new JLabel("White Moves: 0");
        blackMovesLabel = new JLabel("Black Moves: 0");
        
        add(totalMovesLabel);
        add(whiteMovesLabel);
        add(blackMovesLabel);
        
        updateStats();
    }
    
    public void updateStats()
    {
        String gameName = Game.gameName;
        if(gameName == null) return;
        
        List<String> moves = PlayerMovesFileIO.loadMoves(gameName);
        
        int total = moves.size();
        int whiteMoves = total / 2 + total % 2;
        int blackMoves = total / 2;
        
        totalMovesLabel.setText("Total Moves: " + total);
        whiteMovesLabel.setText("White Moves: " + whiteMoves);
        blackMovesLabel.setText("Black Moves: " + blackMoves);
    }
}
