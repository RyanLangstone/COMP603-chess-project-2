/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Explosive
 */
public class LogsPanel extends JPanel {
    private JTextArea logArea;
    private JButton loadButton;
    private JButton backButton;
   private Game game;
    
    public LogsPanel(Game game)
    {
        this.game = game;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Move Log"));
        
        //Text area setup
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);
        
        //Bottom button Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
       
        
        loadButton = new JButton("Load log File");
        backButton = new JButton("Back to Menu");
        
        bottomPanel.add(loadButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        //Button action: Open file chooser
        loadButton.addActionListener(e -> chooseAndLoadLogFile());
        backButton.addActionListener(e -> game.showPanel("Menu"));
        
        //Loads the current games moves if they are available
        loadMovesFromFile(Game.gameName);
        
    }
    
    //Loads moves for the specified game name (if it exists)
    public void loadMovesFromFile(String gameName)
    {
        logArea.setText(""); //clear existing text
        
        if(gameName == null || gameName.isEmpty())
        {
            logArea.setText("No game loaded. \n");
            return;
        }
        
        List<String> moves = PlayerMovesFileIO.loadMoves(gameName);
        if(moves.isEmpty())
        {
            logArea.append("No moves found for game: " + gameName + "\n");
        } else {
            logArea.append("Loaded moves for game: " + gameName + "\n\n");
        for (String move : moves)
        {
            logArea.append(move + "\n");
        }
        }
    }
    
    //Lets the user select a log file manually using JFileChooser
    private void chooseAndLoadLogFile()
    {
        JFileChooser fileChooser = new JFileChooser("moves");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            
            //Extract the game name from "(gameName)_moves.txt"
            String gameName = fileName.replace("_moves.txt", "");
            loadMovesFromFile(gameName);
        }
    }
}


