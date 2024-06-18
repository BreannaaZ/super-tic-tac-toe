package Project2;

/**
 * SuperTicTacToe starts the program from the main method located here
 * and holds the outermost frame of the GUI.
 *
 * @authors Kara Byers, Darren Fife, Breanna Zinky
 * @date 10/18/22
 * @version 1.0
 */

import javax.swing.*;

/************************************************************/
/* Class */
public class SuperTicTacToe {
    /************************************************************/
    /* Variables */
    // size of the board for the game
    public static int boardSize;
    // number of connections needed to win
    public static int numToWin;
    // Using Cell enum (X, O, Empty)
    public Cell whoStarts;
    /************************************************************/
    /* Main Method */

    /**
     * Starts the SuperTicTacToe game. Creates the outermost frame and creates
     * the other objects
     * Takes input from the user to determine board size, number of connections
     * to win, and who starts first (X or O).
     *
     * @param args Arguments for main method
     */
    public static void main(String[] args) {
        // This main method will create a JFrame object with the title "Super Tic-Tac-Toe and add it to SuperTicTacToePanel
        // It will also create a SuperTicTacToeGame game object game = new SuperTicTacToeGame();
/************************************************************/
        /* Instantiate Variables */
        boardSize = 0;
        numToWin = 0;
        // Using Cell enum (X, O, Empty)
        Cell startingPlayer = null;
/************************************************************/
        JFrame frame = new JFrame("Super Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/************************************************************/
        /* User Input Section - Get Board Size */

        // Retry boolean is used in a while loop to repeatedly ask the user for input if
        // invalid amounts are entered.
        boolean retry = true;
        while (retry) {
            try {
                // Prompt user for input for board size
                String inputString = JOptionPane.showInputDialog(null, "Enter in the size of the board: ");

                // Exit the system if the cancel button was pressed by checking if the input is null
                // (if it is null, then the cancel button was pressed - otherwise it will contain an empty string)
                if (inputString == null) {
                    System.exit(JFrame.DO_NOTHING_ON_CLOSE);
                }

                // Get the boardSize from parsing the input string
                boardSize = Integer.parseInt(inputString); // If invalid amount is entered, an exception is caused here

                // Validate that the input is between 2 and 15.
                if (boardSize <= 2 || boardSize >= 15) {
                    throw new NumberFormatException();
                }
                retry = false; // If it makes it here, the correct input was entered.
            }
            // Catch and display error message for invalid input
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a number between 2 and 15.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
/************************************************************/
        /* User Input Section - Get Number of Connections to Win */

        // reset retry boolean
        retry = true;
        while (retry) {
            try {
                // Prompt user for input for number of connections to win
                String inputString = JOptionPane.showInputDialog(null, "Enter in the amount of connections to win: ");

                // Exit the system if the cancel button was pressed by checking if the input is null
                // (if it is null, then the cancel button was pressed - otherwise it will contain an empty string)
                if (inputString == null) {
                    System.exit(0);
                }

                // Get the numToWin from parsing the input string
                numToWin = Integer.parseInt(inputString); // If invalid amount is entered, an exception is caused here

                // If the board size is 3 then the number to win must be 3
                // If it isn't, throw an exception
                if( boardSize == 3 && numToWin < 3) {
                    throw new NumberFormatException();
                }

                // If the board size is greater than 3, the number to win must be greater than 3
                // If it isn't, throw an exception
                if (boardSize > 3 && numToWin <= 3) {
                    throw new NumberFormatException();
                }

                // The number to win must be less than the board size - if it isn't, throw an exception
                if (numToWin > boardSize) {
                    throw new NumberFormatException();
                }

                retry = false; // If it makes it here, the correct input was entered.
            }

            // Catch and display error message for invalid input
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number. If board size is 3, connections to win must be 3. Otherwise, connections to win must be less than or equal to the board size and greater than 3.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
/************************************************************/
        /* User Input Section - Get Who Starts First (X or O) */

        // reset retry boolean
        retry = true;
        while (retry) {
            try {
                // Prompt user for input for number of connections to win
                String inputString = JOptionPane.showInputDialog(null, "Who starts first? X or O: ");

                // Exit the system if the cancel button was pressed by checking if the input is null
                // (if it is null, then the cancel button was pressed - otherwise it will contain an empty string)
                if (inputString == null) {
                    System.exit(JFrame.EXIT_ON_CLOSE);
                }

                // Make inputString uppercase so if a lowercase "x" or "o" is entered it's still valid
                inputString = inputString.toUpperCase();

                // Remove any whitespace from inputString so inputs "X " or " O" would be valid
                inputString = inputString.replaceAll("\\s", "");

                // Check that inputString is either X or O
                if (!inputString.equals("X") && !inputString.equals("O")) {
                    throw new NumberFormatException();
                }

                // Set startingPlayer using the selected Cell X or O enum
                startingPlayer = Cell.valueOf(inputString);

                retry = false; // If it makes it here, the correct input was entered.
            }
            // Catch and display error message for invalid input
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter either X or O.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
/************************************************************/
        // Create the main game object to have better MVC
        SuperTicTacToeGame game = new SuperTicTacToeGame(boardSize, numToWin, startingPlayer);

        /* Send Frame */
        // Send game to SuperTicTacToePanel constructor using the user's input
        frame.getContentPane().add(new SuperTicTacToePanel(game, boardSize, numToWin, startingPlayer));
        frame.pack();
        frame.setVisible(true);
    } // End of main method
} // End of class SuperTicTacToe


