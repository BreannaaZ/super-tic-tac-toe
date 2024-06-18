package Project2;

/**
 * SuperTicTacToePanel is the gui panel that houses the model part of the game that is adaptive to user input.
 *
 *  @authors Kara Byers, Darren Fife, Breanna Zinky
 *  @date 10/18/22
 *  @version 1.0
 */

/************************************************************/
/* Imports */

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/************************************************************/
/* Class */
public class SuperTicTacToePanel extends JPanel {
    /************************************************************/
    /* Variables */
    private SuperTicTacToeGame game; // Main game instance variable.
    private int boardSize; // Size of the board
    private int numToWin; // Number of spaces in a row needed to win.
    private Cell startingPlayer; // Which symbol is going first.
    private int xWinCount; // Counter for how many times X has won (to be displayed on GUI)
    private int oWinCount; // Counter for how many times O has won (to be displayed on GUI)

    private JButton[][] jButtonBoard; // 2D array representing the GUI board the user sees
    private Cell[][] iBoard; // 2D array of Cell received from the game object representing the TicTacToe board
    private JButton quitButton; // Button allows you to quit the game
    private JButton undoButton; // Button allows you to undo to the previous move
    private ImageIcon xIcon; // x icon shown on the GUI board
    private ImageIcon oIcon; // o icon shown on the GUI board
    private ImageIcon emptyIcon; // Empty (free space) icon shown on the GUI board
    private JLabel xWinCounter; // Label to hold xWinCount
    private JLabel oWinCounter; // Label to hold oWinCount

    private JPanel buttonPanel; // Panel to hold the jButtonBoard buttons

    /************************************************************/
    /* Constructor */

    /**
     * Parameterized Constructor
     * Super TicTacToe constructor that generates a board based on user input.
     *
     * @param game           Main game instance
     * @param boardSize      Size of board being created.
     * @param numToWin       Number of spaces in a row needed to win.
     * @param startingPlayer Which symbol is going first.
     */
    public SuperTicTacToePanel(SuperTicTacToeGame game, int boardSize, int numToWin, Cell startingPlayer) {
        // Set the parameters
        this.game = game;
        this.boardSize = boardSize;
        this.numToWin = numToWin;
        this.startingPlayer = startingPlayer;

        // Instantiate the Image Icons
        xIcon = new ImageIcon("src/Project2/X.png");
        oIcon = new ImageIcon("src/Project2/O.png");
        emptyIcon = new ImageIcon("src/Project2/emptyIcon.png");

        // Resize the image icons to fit the button's size (which depends on how big the board is)
        Image xIconResized = xIcon.getImage(); //Transform the ImageIcon to an image so that we can resize it
        Image oIconResized = oIcon.getImage(); //Transform the ImageIcon to an image so that we can resize it
        if (boardSize < 7) {
            Image xIconResized2 = xIconResized.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            Image oIconResized2 = oIconResized.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            xIcon = new ImageIcon(xIconResized2); //Transform the resized image back into an ImageIcon
            oIcon = new ImageIcon(oIconResized2); //Transform the resized image back into an ImageIcon
        } else if (boardSize < 10) {
            Image xIconResized2 = xIconResized.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
            Image oIconResized2 = oIconResized.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
            xIcon = new ImageIcon(xIconResized2); //Transform the resized image back into an ImageIcon
            oIcon = new ImageIcon(oIconResized2); //Transform the resized image back into an ImageIcon
        } else if (boardSize < 13) {
            Image xIconResized2 = xIconResized.getScaledInstance(55, 55, java.awt.Image.SCALE_SMOOTH);
            Image oIconResized2 = oIconResized.getScaledInstance(55, 55, java.awt.Image.SCALE_SMOOTH);
            xIcon = new ImageIcon(xIconResized2); //Transform the resized image back into an ImageIcon
            oIcon = new ImageIcon(oIconResized2); //Transform the resized image back into an ImageIcon
        } else {
            Image xIconResized2 = xIconResized.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            Image oIconResized2 = oIconResized.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            xIcon = new ImageIcon(xIconResized2); //Transform the resized image back into an ImageIcon
            oIcon = new ImageIcon(oIconResized2); //Transform the resized image back into an ImageIcon
        }

        // Create a new panel to hold the board buttons
        buttonPanel = new JPanel();

        // Set the buttonPanel to a grid layout
        buttonPanel.setLayout(new GridLayout(boardSize, boardSize, 5, 5));
        buttonPanel.setBackground(Color.black);

        // Undo button creation
        undoButton = new JButton("UNDO");
        undoButton.setBackground(Color.white);
        undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action listener for undo button (calls the undo method in SuperTicTacToeGame)
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    game.undo();
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, exc.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                // Call display board to refresh board and update the icons
                displayBoard();
            }
        });

        // Quit button creation
        quitButton = new JButton("QUIT");
        quitButton.setBackground(new Color(250, 127, 127));
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action listener for quit button (will close application after confirmation)
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit Super-Tic-Tac-Toe?", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Reset button creation
        JButton resetButton = new JButton("RESET");
        resetButton.setBackground(Color.white);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action listener for reset button (calls reset method in STTTG to clear board)
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call helper method to reset game
                resetGame();
                // Call display board to refresh board and update the icons
                displayBoard();
            }
        });

        // Play computer button creation
        JButton playCompButton = new JButton("PLAY COMPUTER");
        playCompButton.setBackground(Color.white);
        playCompButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playCompButton.setMargin(new Insets(3, 3, 3, 3));

        // Action listener for play computer button (calls the play computer method in STTTG)
        playCompButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.enableComputer();
                // Call display board to refresh board and update the icons
                displayBoard();
            }
        });

        // Title JLabel creation
        JLabel Title = new JLabel("<html><font size = '18' color = #FF0033> Super </font> <font size = '18' color = #3366FF>Tic-</font><font size = '18' color = #FF0033>Tac-</font><font size = '18' color = #3366FF>Toe</html>");
        Title.setFont(new Font("Verdana", Font.BOLD, 18));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        Title.setBounds(15, 45, 400, 700);

        // Win count JLabel creation
        int xWinCount = 0;
        int oWinCount = 0;
        JLabel winCount = new JLabel("WIN COUNT");
        winCount.setFont(new Font("Verdana", Font.BOLD, 12));
        winCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        xWinCounter = new JLabel("X: " + xWinCount);
        oWinCounter = new JLabel("O: " + oWinCount);

        // Create panel to hold X and O counter so they are side-by-side
        JPanel Counters = new JPanel();
        Counters.add(xWinCounter);
        Counters.add(oWinCounter);
        Counters.setMaximumSize(new Dimension(100, 30));

        // Side panel creation to hold win count and other buttons
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.add(Box.createRigidArea(new Dimension(10, 10))); // Vertical empty space holder
        sidePanel.add(winCount);
        sidePanel.add(Counters);
        sidePanel.add(undoButton);
        sidePanel.add(Box.createRigidArea(new Dimension(10, 10))); // Vertical empty space between buttons
        sidePanel.add(resetButton);
        sidePanel.add(Box.createRigidArea(new Dimension(10, 10))); // Vertical empty space between buttons
        sidePanel.add(playCompButton);
        sidePanel.add(Box.createRigidArea(new Dimension(10, 10))); // Vertical empty space between buttons
        sidePanel.add(quitButton);

        // Create a main panel to hold the tic-tac-toe board (buttonPanel) as well as the side panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(Title, BorderLayout.NORTH);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        this.add(mainPanel); // Add the main panel to the main GUI frame

        // Initialize array to board size
        jButtonBoard = new JButton[boardSize][boardSize];

        // Based off the book example
        // Create a ButtonListener to listen to all the separate jButtonBoard buttons
        ButtonListener listener = new ButtonListener();

        // Create the buttons and give action listeners to each
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                jButtonBoard[row][col] = new JButton("", emptyIcon);
                // Resize the buttons depending on how many there are (so they will fit nicely, and so the image icons will
                // be the right size on the buttons.)
                if (boardSize < 7) {
                    jButtonBoard[row][col].setPreferredSize(new Dimension(100, 100));
                } else if (boardSize < 10) {
                    jButtonBoard[row][col].setPreferredSize(new Dimension(80, 80));
                } else if (boardSize < 13) {
                    jButtonBoard[row][col].setPreferredSize(new Dimension(55, 55));
                } else {
                    jButtonBoard[row][col].setPreferredSize(new Dimension(50, 50));
                }

                // Add action listeners to the buttons
                jButtonBoard[row][col].addActionListener(listener);

                // Remove the button's borders (to look cleaner)
                jButtonBoard[row][col].setBorderPainted(false);

                // Add the buttons to the buttonPanel
                buttonPanel.add(jButtonBoard[row][col]);
            }
        }
    } // End of Panel method

    /**
     * Resets the board while showing message about computer player being disabled.
     * Helper method for code reuse.
     */
    private void resetGame() {
        if (game.isComputerPlayerEnabled()) {
            JOptionPane.showMessageDialog(null, "Computer player has been disabled.");
        }

        game.reset();
    }

    /************************************************************/
    /* displayBoard Method */

    /**
     * Displays the GUI board's icons based on the state of the game board.
     */
    private void displayBoard() {
        // This method will first call the getBoard() method within the game class to get the TicTacToe board.
        // Then, it will use a nested loop to set the icon to the JButtons within the GUI. Example in project outline.

        iBoard = game.getBoard();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Cell currentCell = iBoard[row][col];

                if (currentCell == Cell.O) {
                    jButtonBoard[row][col].setIcon(oIcon);
                } else if (currentCell == Cell.X) {
                    jButtonBoard[row][col].setIcon(xIcon);
                } else {
                    jButtonBoard[row][col].setIcon(emptyIcon);
                }
            }
        }
    } // End of displayBoard method

    /* Private inner Class */

    /**
     * Private inner class that Listens for Button events, and does actions based on the events.
     *
     * @version 1.0
     * @authors Kara Byers, Darren Fife, Breanna Zinky
     * @date 10/18/22
     */
    private class ButtonListener implements ActionListener {

        /************************************************************/
        /* actionPerformed Method */

        /**
         * Processes events to run actions based on the button pressed.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // This method will call the different methods in the TicTacToeGame class using the game object.
            // Ex. pressing a specific button will call the related method.
            // Code example in project outline.
            // Determine which button was selected.
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    if (jButtonBoard[row][col] == e.getSource())
                    // tell the game which button was selected.
                    {
                        game.playerSelect(row, col);
                    }
                }
            }

            // Display the board using the private method describe above.
            displayBoard();

            // Determine if there is a winner by asking the game object
            if (game.getGameStatus() == GameStatus.O_WON) {
                JOptionPane.showMessageDialog(null, "O won and X lost! The game will reset.");
                // Call helper method to reset game
                resetGame();
                // Call display board to refresh board and update the icons
                displayBoard();
                oWinCount++; // Add to O win counter
                oWinCounter.setText("O: " + oWinCount); // Update label holding O win count
            } else if (game.getGameStatus() == GameStatus.X_WON) {
                JOptionPane.showMessageDialog(null, "X won and O lost! The game will reset.");
                // Call helper method to reset game
                resetGame();
                // Call display board to refresh board and update the icons
                displayBoard();
                xWinCount++; // Add to X win counter
                xWinCounter.setText("X: " + xWinCount); // Update label holding X win count
            } else if (game.getGameStatus() == GameStatus.CATS) {
                JOptionPane.showMessageDialog(null, "Tie Game. The game will reset.");
                // Call helper method to reset game
                resetGame();
                // Call display board to refresh board and update the icons
                displayBoard();
            }
        } // End of actionPerformed method
    } // End of class ButtonListener
} // End of class SuperTicTacToePanel

