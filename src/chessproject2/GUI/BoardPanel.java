/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package chessproject2.GUI;

import chessproject2.BoardFileIO;
import static chessproject2.Game.isCheckmate;
import static chessproject2.Game.isSquareAttacked;
import chessproject2.Pieces.Piece;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author RyanL
 */
public class BoardPanel extends javax.swing.JPanel {

    public static int turn;
    private Piece[][] board;
    private final int tileSize = 70;
    private int selectedRow = -1, selectedCol = -1;
    private final MouseAdapter mouseHandler;

    public BoardPanel() {
        this(BoardFileIO.loadDefaultBoard(), 0);    //if new game is created and want to use default borad, this constructor will be called
    }

    public BoardPanel(Piece[][] board, int turn) {
        initComponents();
        int count = 0;
        System.out.println("Non-null pieces in board: " + count);
        this.board = board;
        // Assign to the static turn field
        BoardPanel.turn = turn;

        // Corrected: Initialize the final mouseHandler field
        mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / tileSize; // as white is drawn at bottom
                int pieceRow = 7 - row;
                int col = e.getX() / tileSize;
                Piece piece = board[pieceRow][col];
                final int currentTurn = BoardPanel.turn; // Use the static turn field here
                if (selectedRow == -1) {
                    //Select a piece
                    if (board[pieceRow][col] != null && (piece.isWhite && currentTurn % 2 == 0 || (!piece.isWhite && currentTurn % 2 == 1))) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    }
                } else {
                    //Try to move selected piece

                    if (board[pieceRow][col] != null && (piece.isWhite && currentTurn % 2 == 0 || (!piece.isWhite && currentTurn % 2 == 1))) {
                        selectedRow = row;
                        selectedCol = col;
                        repaint();
                    } else {
                        movePiece(7 - selectedRow, selectedCol, pieceRow, col);
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint();
                    }
                }
            }
        }; // Semicolon terminates the assignment.

        // Add the initialized handler to the panel.
        addMouseListener(mouseHandler);
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

                //Special rules
                if (piece.type == "Pawn" && toCol != fromCol && board[toRow][toCol] == null) {
                    board[fromRow][toCol] = null; //capture the En Passant piece
                }

                //checks castling
                if (piece.type == "King" && toCol - fromCol == 2) {
                    board[toRow][toCol - 1] = board[toRow][7];
                    board[toRow][toCol - 1].setPosition(toRow, toCol + 1);
                    board[toRow][7] = null;
                } else if (piece.type == "King" && toRow - fromCol == -2) {
                    board[toRow][toCol + 1] = board[toRow][0];
                    board[toRow][toCol + 1].setPosition(toRow, toCol - 1);
                    board[toRow][0] = null;
                }

                // === CHECK / CHECKMATE HANDLING ===
                boolean opponentIsWhite = !piece.isWhite;
                if (isCheckmate(opponentIsWhite, board)) { // <-- ADDED
                    String winnerName = piece.isWhite ? "White" : "Black";
                    String loserName = piece.isWhite ? "Black" : "White";
                    System.out.println("CHECKMATE! " + winnerName + " wins!");
                    // 1. Remove the mouse listener to prevent further input
                    removeMouseListener(mouseHandler);

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

                piece.turnMoved = turn;
                turn++;

                //get top parent frame
                GameFrame frame = (GameFrame) SwingUtilities.getWindowAncestor(this);
                //  Safely call the update method
                if (frame != null) {
                    frame.updateTurnLabel(turn);
                }
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Calculate valid moves ONCE if a piece is selected
        int[][] movesToHighlight = null;
        if (selectedRow != -1 && selectedCol != -1) {
            // Remember to use the board-friendly coordinates (0-7 from black to white)
            Piece selectedPiece = board[7 - selectedRow][selectedCol];
            if (selectedPiece != null) {
                movesToHighlight = selectedPiece.ValidMoves(board, true);
            }
        }

        //Draw the 8x8 board
        for (int row = 0; row < 8; row++) {
            int pieceRow = 7 - row;
            for (int col = 0; col < 8; col++) {
                boolean light = (row + col) % 2 == 0;
                g.setColor(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

                //Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g.setColor(new Color(255, 255, 0, 100));
                    g.fillRect(col * tileSize + 5, row * tileSize + 5, tileSize - 10, tileSize - 10);

                }
                //highlight valid move squares
                if (movesToHighlight != null) {
                    // Check if the current board square (row, col) matches any valid move target
                    for (int[] move : movesToHighlight) {
                        // move[0] is board row (0-7), move[1] is board col (0-7)
                        // The screen row is 7 - move[0] (because the screen draws from top-to-bottom)
                        int targetScreenRow = 7 - move[0];
                        int targetScreenCol = move[1];

                        // If the current square being drawn is a valid move target
                        if (row == targetScreenRow && col == targetScreenCol) {
                            g.setColor(new Color(0, 255, 0, 100)); // Transparent Green
                            g.fillRect(col * tileSize + 5, row * tileSize + 5, tileSize - 10, tileSize - 10);
                            break; // Stop checking valid moves for this square
                        }
                    }
                }

                //Draw piece (if any)
                Piece piece = board[pieceRow][col];
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
