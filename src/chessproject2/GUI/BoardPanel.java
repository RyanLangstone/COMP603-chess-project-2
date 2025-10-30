/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package chessproject2.GUI;

import chessproject2.BoardFileIO;
import chessproject2.Pieces.Piece;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author RyanL
 */
public class BoardPanel extends javax.swing.JPanel {

    public static int turn;
    private Piece[][] board;
    private final int tileSize = 70;
    private int selectedRow = -1, selectedCol = -1;

    public BoardPanel() {
        this(BoardFileIO.loadDefaultBoard(), 0);    //if new game is created and want to use default borad, this constructor will be called
    }

    public BoardPanel(Piece[][] board, int turn) {
        initComponents();
        int count = 0;
for (int r = 0; r < board.length; r++) {
    for (int c = 0; c < board[r].length; c++) {
        if (board[r][c] != null) count++;
    }
}
System.out.println("Non-null pieces in board: " + count);
        this.board = board;
        this.turn = turn;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / tileSize;
                int col = e.getX() / tileSize;

                if (selectedRow == -1) {
                    //Select a piece
                    if (board[row][col] != null) {
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board[fromRow][fromCol];
        if (piece == null) {
            return;
        }

        int[][] validMoves = piece.ValidMoves(board, true);
        for (int[] move : validMoves) {
            if (move[0] == toRow && move[1] == toCol) {
                board[toRow][toCol] = piece;
                board[fromRow][fromCol] = null;
                piece.setPosition(toRow, toCol);
                piece.turnMoved = turn;
                turn++;
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draw the 8x8 board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boolean light = (row + col) % 2 == 0;
                g.setColor(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

                //Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g.setColor(new Color(255, 255, 0, 100));
                    g.fillRect(col * tileSize + 5, row * tileSize + 5, tileSize - 10, tileSize - 10);
                }

                //Draw piece (if any)
                Piece piece = board[row][col];
                if (piece != null) {
                    g.setFont(new Font("SansSerif", Font.PLAIN, 48));
                    g.setColor(piece.isWhite() ? Color.WHITE : Color.BLACK);
                    g.drawString(piece.unicode, col * tileSize + 10, row * tileSize + 55);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(8 * tileSize, 8 * tileSize);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
