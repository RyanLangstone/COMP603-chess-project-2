package chessproject2;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Main game logic for chess Handles initialization, menus, players, board
 * rendering, move validation, and special rules
 *
 * @author RyanL and Yaacoub
 */
public class Game extends JFrame {
   public static Piece[][] board = new Piece[8][8];
   public static String gameName, whiteName, blackName;
   public static int turn = 1;
   
   private Panel gamePanel;
   private CardLayout cardLayout;
   private JPanel mainPanel;
   private MainMenuPanel menuPanel;
   private PlayerSetupPanel playerSetupPanel;
   private InstructionsPanel instructionsPanel;
   private StatsPanel statsPanel;
   private LogsPanel logsPanel;
   
   public Game()
   {
       setTitle("Chess GUI");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(900, 900);
       setLocationRelativeTo(null);
       
       cardLayout = new CardLayout();
       mainPanel = new JPanel(cardLayout);
       
       //Panels
       menuPanel = new MainMenuPanel(this);
       playerSetupPanel = new PlayerSetupPanel(this);
       instructionsPanel = new InstructionsPanel(this);
       statsPanel = new StatsPanel(this);
       logsPanel = new LogsPanel(this);
       
       //Game panel is created when starting a new or loaded game
       gamePanel = new Panel(board, this);
       
       //Adds all panels to card Layout
       mainPanel.add(menuPanel, "Menu");
       mainPanel.add(playerSetupPanel, "PlayerSetup");
       mainPanel.add(instructionsPanel, "Instructions");
       mainPanel.add(statsPanel, "Stats");
       mainPanel.add(logsPanel, "Logs");
       mainPanel.add(gamePanel, "Game");
       
       add(mainPanel);
       showPanel("Menu");
       setVisible(true);
   }
       
   public void showPanel(String name)
   {
       cardLayout.show(mainPanel, name);
   }
   
   public void startNewGame(String gName, String white, String black)
   {
       gameName = gName;
       whiteName = white;
       blackName = black;
       turn = 1;
       
       BoardFileIO.loadDefaultBoard(); //Populate board
       
       gamePanel = new Panel(board, this);
       mainPanel.add(gamePanel, "Game");
       showPanel("Game");
   }
   
   public static int getTurn()
   {
       return turn;
   }
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
