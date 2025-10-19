/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessproject2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Explosive
 */
public class Panel extends JPanel {
 private final Piece[][] board;
 private final Game game;
 private final int tileSize = 80;
 private int selectedRow = -1, selectedCol = -1;
 
 public Panel(Piece[][] board, Game game)
 {
     this.board = board;
     this.game = game;
     
     addMouseListener(new MouseAdapter() 
     {
         @Override
         public void mousePressed(MouseEvent e)
         {
             int row = e.getY() / tileSize;
             int col = e.getX() / tileSize;
             
             if (selectedRow == -1)
             {
                 //Select a piece
                 if(board[row][col] != null)
                 {
                     selectedRow = row;
                     selectedCol = col;
                     repaint();
                 }
             } else {
                 //Try to move selected piece
                 movePiece(selectedRow, selectedCol, row, col);
                 selectedRow = -1;
                 selectedCol = -1;
                 repaint();
             }
         }
     });
 }
    private void movePiece(int fromRow, int fromCol, int toRow, int toCol)
    {
        Piece piece = board[fromRow][fromCol];
        if (piece == null) return;
        
        int[][] validMoves = piece.ValidMoves(board);
        for (int[] move : validMoves)
        {
            if(move[0] == toRow && move[1] == toCol)
            {
                board[toRow][toCol] = piece;
                board[fromRow][fromCol] = null;
                piece.setPosition(toRow, toCol);
                piece.turnMoved = Game.getTurn();
                Game.turn++;
                return;
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        //Draw the 8x8 board
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                boolean light = (row + col) % 2 == 0;
                g.setColor(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                
                //Highlight selected square
                if(row == selectedRow && col == selectedCol)
                {
                    g.setColor(new Color(255, 255, 0, 100));
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }
                
                //Draw piece (if any)
                Piece piece = board[row][col];
                if (piece != null)
                {
                    g.setFont(new Font("SansSerif", Font.PLAIN, 48));
                    g.setColor(piece.isWhite() ? Color.WHITE : Color.BLACK);
                    g.drawString(piece.unicode, col * tileSize + 20, row * tileSize + 55);
                }
            }
        }
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(8 * tileSize, 8 * tileSize);
    }
}
