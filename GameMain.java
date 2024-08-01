import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel implements MouseListener {
    // Constants for game
    // Number of ROWS by COLS cell constants
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final String TITLE = "Tic Tac Toe";

    // Constants for dimensions used for drawing
    // Cell width and height
    public static final int CELL_SIZE = 100;
    // Drawing canvas
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    // Noughts and Crosses are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    /* Declare game object variables */
    // Game board
    private Board board;
    
    // Enumeration for game states
    private enum GameState {
        Playing, Draw, Cross_won, Nought_won
    }
    
    // Current game state
    private GameState currentState;
    // Current player
    private Player currentPlayer;
    // For displaying game status message
    private JLabel statusBar;

    /* Constructor to setup the UI and game components on the panel */
    public GameMain() {
        // Add mouse listener to detect clicks on the game board
        this.addMouseListener(this);
        
        // Setup the status bar to display status message
        statusBar = new JLabel("         ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);

        // Use BorderLayout to place the status bar at the bottom
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        // Account for statusBar height in overall height
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

        // Create a new instance of the game board class
        board = new Board();

        // Initialize the game board
        initGame();
    }
    
    public static void main(String[] args) {
        // Run GUI code in Event Dispatch thread for safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create a main window to contain the game panel
                JFrame frame = new JFrame(TITLE);
                
                // Create and add the game panel to the frame
                frame.add(new GameMain());
                
                // Set the default close operation of the frame to exit on close
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    /* Custom painting codes on this JPanel */
    public void paintComponent(Graphics g) {
        // Fill background and set colour to white
        super.paintComponent(g);
        setBackground(Color.WHITE);
        // Ask the game board to paint itself
        board.paint(g);
        
        // Update the status bar based on the current game state
        if (currentState == GameState.Playing) {
            statusBar.setForeground(Color.BLACK);
            if (currentPlayer == Player.Cross) {
                // Use the status bar to display the message "X's Turn
                statusBar.setText("X's Turn");
            } else {
                // Use the status bar to display the message "O's Turn
                statusBar.setText("O's Turn");
            }
        } else if (currentState == GameState.Draw) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == GameState.Cross_won) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == GameState.Nought_won) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }
    
    /* Initialise the game-board contents and the current status of GameState and Player) */
    public void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board.cells[row][col].clear();
            }
        }
        currentState = GameState.Playing;
        currentPlayer = Player.Cross;
    }
    
    /* After each turn check to see if there has been a win or a draw */
    public void updateGame(Player thePlayer, int row, int col) {
        if (board.hasWon(thePlayer, row, col)) {
            // Check which player has won and update the current state to the appropriate game state
            if (thePlayer == Player.Cross) {
                currentState = GameState.Cross_won;
            } else {
                currentState = GameState.Nought_won;
            }
        } else if (board.isDraw()) {
            // Set the current state to the draw game state
            currentState = GameState.Draw;
        }
    }
    
    /* Event handler for mouse click events on the JPanel */
    public void mouseClicked(MouseEvent e) {
        // Get coordinates of where the click event happened
        int mouseX = e.getX();
        int mouseY = e.getY();
        // Get the row and column clicked
        int rowSelected = mouseY / CELL_SIZE;
        int colSelected = mouseX / CELL_SIZE;
        if (currentState == GameState.Playing) {
            if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
                board.cells[rowSelected][colSelected].content = currentPlayer;
                // Update current state
                updateGame(currentPlayer, rowSelected, colSelected);
                // Switch player
                currentPlayer = (currentPlayer == Player.Cross) ? Player.Nought : Player.Cross;
            }
        } else {
            // Game over and restart
            initGame();
        }
        // Redraw the graphics on the UI
        repaint();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
