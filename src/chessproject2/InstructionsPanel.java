/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Explosive
 */
public class InstructionsPanel extends JPanel {
    public InstructionsPanel(Game game)
    {
        setLayout(new BorderLayout());
        JTextArea area = new JTextArea("""
         Instructions:
         1. Choose to start a new game or load an existing one
         2. Select player names for white and black and a game Name
         3. Click on pieces to move them
         4. Checkmate the opponent to win! 
          """);
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
        JButton back = new JButton("Back");
        back.addActionListener(e -> game.showPanel("Menu"));
        add(back, BorderLayout.SOUTH);
    }
}
