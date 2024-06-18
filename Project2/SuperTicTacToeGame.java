package Project2;

/**
 * SuperTicTacToeGame contains the game logic to play TicTacToe on a user defined sized board.
 * This method creates the game board, holds the logic of selecting a cell, determining who won,
 * and logic for a computer player.
 *
 *  @authors Kara Byers, Darren Fife, Breanna Zinky
 *  @date 10/18/22
 *  @version 1.0
 */

/************************************************************/
/* Imports */

import java.util.ArrayList;
import java.awt.Point;

/************************************************************/
/* Class */
public class SuperTicTacToeGame {
    /************************************************************/
    /* Variables */
    // 2D array of cells that compose the game board
    private Cell[][] board;

    // enum of the status of the game
    private GameStatus status;
    private ArrayList<Point> point; // Use the class called "Point" from Java API
    // This will be used to save the user's position each time and store
    // all previously selected positions so moves can be undone

    // enum of what cell type the current player is
    private Cell currentPlayer;

    // boolean for whether the computer player is enabled
    private boolean computerIsPlaying = false;

    // keeps track of what cell each player is
    private Cell computerPlayer = Cell.EMPTY;
    private Cell player = Cell.EMPTY;

    // counter of how many turns have been taken
    private int numTurns;

    // number of connections in a row needed to win
    private int numToWin;
    // size of the board
    private int lastBoardSize;
    // which player starts the game
    private Cell startPlayer;

    // counters for how many X/O's in a row
    private int xCount = 0;
    private int oCount = 0;

    /************************************************************/
    /* Constructors */

    /**
     * Generic constructor that creates a standard 3x3 TicTacToe game starting with X and requiring 3 in a row to win.
     */
    public SuperTicTacToeGame() {
        this(3, 3, Cell.X);
    } // End of constructor

    /**
     * Parameterized Constructor that takes board size to create a TicTacToe game.
     *
     * @param boardSize      integer number for size of square TicTacToe board
     * @param numToWin       integer number of how many Xs or Os in a row to win the game
     * @param startingPlayer Cell enumeration for if O or X is starting
     */
    public SuperTicTacToeGame(int boardSize, int numToWin, Cell startingPlayer) {
        super();

        status = GameStatus.IN_PROGRESS;
        board = new Cell[boardSize][boardSize];

        // Initialize board buttons to empty
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = Cell.EMPTY;
            }
        }

        currentPlayer = startingPlayer;
        startPlayer = startingPlayer;
        this.numToWin = numToWin;
        lastBoardSize = boardSize;
        numTurns = 0;
        xCount = 0;
        oCount = 0;
        point = new ArrayList<>();

    } // End of constructor

    /************************************************************/
    /**
     * Calls select method after player clicks, then computer plays if it is enabled.
     *
     * @param row int row position of the cell selected
     * @param col int column position of the cell selected
     */
    public void playerSelect(int row, int col) {
        select(row, col);

        if (computerIsPlaying && currentPlayer == computerPlayer) {
            playComputerTurn();
        }
    }

    /* Select Method */

    /**
     * Select method uses the Cell enum to mark the selected cell at row, col as either X or O.
     * Also changes who the current player is after the move is made.
     *
     * @param row int row position of the cell selected
     * @param col int column position of the cell selected
     */
    public void select(int row, int col) {
        // This method will be invoked when the user selects a JButton within the 2D array at location (row, col).
        // This method will use the Cell enum to mark a cell as either Cell.X or Cell.O
        // Ex. board[row][col] = Cell.O;

        // Only change/select empty cells
        if (board[row][col] == Cell.EMPTY) {
            // Sets the selected cell as the currentPlayer's Cell
            board[row][col] = currentPlayer;

            // Changes who is the current player after the selection (move) is made
            if (currentPlayer == Cell.O) {
                currentPlayer = Cell.X;
            } else {
                currentPlayer = Cell.O;
            }
            // increment turn counter
            numTurns++;
            // Add selected position to point array list to save it
            point.add(new Point(row, col));
        }

        // Call the game status to check for a winner
        getGameStatus();
    } // End of Select Method

    /************************************************************/
    /* Reset Method */

    /**
     * Reset method resets the board to empty for a new game using the values entered for starting player, board size,
     * and num to win from the initial game.
     */
    public void reset() {
        // This method is called from the SuperTicTacToePanel class and resets the board to empty for a new game.
        status = GameStatus.IN_PROGRESS;

        board = new Cell[lastBoardSize][lastBoardSize];
        for (int row = 0; row < lastBoardSize; row++) {
            for (int col = 0; col < lastBoardSize; col++) {
                board[row][col] = Cell.EMPTY;
            }
        }
        currentPlayer = startPlayer;
        numTurns = 0;
        xCount = 0;
        oCount = 0;

        // Reset the point array
        point.clear();

        if (computerIsPlaying) {
            computerIsPlaying = false;
        }
    } // End of Reset Method

    /************************************************************/
    /* getGameStatus Method */

    /**
     * getGameStatus determines if a player has won the game by checking if there are (user's entered numToWin) Xs or Os
     * in a row or if the board is full and there is no winner, result in a tie.
     * Returns the enum for the status of the game.
     *
     * @return GameStatus current status of the game.
     */
    public GameStatus getGameStatus() {
        // This method is called from the SuperTicTacToePanel class and returns the enum for if a
        // player has won the game after the select method was called.
        // It will check if there are (user's entered numToWin) Xs or Os in a row to find the winner, or result in a tie.

        status = getGameStatusOfSpecifiedBoard(board);

        return status;
    } // End of getGameStatus Method

    /**
     * Helper method that checks if the board being checked has a game won.
     *
     * @param boardBeingChecked The board being checked currently.
     * @return Status of the game being checked.
     */
    private GameStatus getGameStatusOfSpecifiedBoard(Cell[][] boardBeingChecked) {
        GameStatus statusOfSpecifiedBoard = GameStatus.IN_PROGRESS;

        // Check rows
        statusOfSpecifiedBoard = checkRows(boardBeingChecked);

        // If no win is found from checking the rows, check columns
        if (statusOfSpecifiedBoard == GameStatus.IN_PROGRESS) {
            statusOfSpecifiedBoard = checkColumns(boardBeingChecked);
        }

        // If no win is found from checking the columns, check the NW-SE diagonals
        if (statusOfSpecifiedBoard == GameStatus.IN_PROGRESS) {
            statusOfSpecifiedBoard = checkDiagonalNWSE(boardBeingChecked);
        }

        // If no win is found from checking the NW-SE diagonals, check the NE-SW diagonals
        if (statusOfSpecifiedBoard == GameStatus.IN_PROGRESS) {
            statusOfSpecifiedBoard = checkDiagonalNESW(boardBeingChecked);
        }

        // If no win is found from checking diagonals and entire board is full, there is a tie
        if (statusOfSpecifiedBoard == GameStatus.IN_PROGRESS && numTurns == lastBoardSize * lastBoardSize) {
            statusOfSpecifiedBoard = GameStatus.CATS;
        }

        return statusOfSpecifiedBoard;
    }

    /************************************************************/
    /* getBoard Method */

    /**
     * GetBoard method returns the current game board.
     *
     * @return board Cell[][] representation of the game board
     */
    public Cell[][] getBoard() {
        // This method returns the board to the SuperTicTacToePanel so the panel can display the board to the user.
        return board;
    } // End of getBoard Method

    /************************************************************/
    /**
     * Enables the computer playing as the current player.
     */
    public void enableComputer() {
        // Check the game is still going
        if (status == GameStatus.IN_PROGRESS) {
            computerIsPlaying = true;

            computerPlayer = Cell.O;
            player = Cell.X;

            if (currentPlayer == computerPlayer) {
                playComputerTurn();
            }
        }
    }

    /**
     * Gets if the computer is playing.
     *
     * @return If the computer is playing.
     */
    public boolean isComputerPlayerEnabled() {
        return computerIsPlaying;
    }

    /* playComputer Method */

    /**
     * Plays the computers turn, even if computer is not enabled.
     * Prioritizes winning and blocking, and then uses algorithm to plan ahead.
     */
    public void playComputerTurn() {
        // Check the game is still going
        if (status == GameStatus.IN_PROGRESS) {
            // Clone board to plan moves
            Cell[][] computerBoard = board;

            int[][] weightedBoard = new int[lastBoardSize][lastBoardSize];

            GameStatus testedStatus = GameStatus.IN_PROGRESS;

            int bestValidMoveRow = -1;
            int bestValidMoveCol = -1;
            int bestMoveValue = 0;

            for (int row = 0; row < lastBoardSize; row++) {
                for (int col = 0; col < lastBoardSize; col++) {
                    // Check if cell is usable
                    if (computerBoard[row][col] == Cell.EMPTY) {
                        // Record move as valid
                        weightedBoard[row][col] = 0;

                        computerBoard[row][col] = computerPlayer;

                        testedStatus = getGameStatusOfSpecifiedBoard(computerBoard);

                        // Check if computer would win placing at position
                        if ((computerPlayer == Cell.X && testedStatus == GameStatus.X_WON) || (computerPlayer == Cell.O && testedStatus == GameStatus.O_WON)) {
                            weightedBoard[row][col] += 200;
                        }

                        computerBoard[row][col] = player;

                        testedStatus = getGameStatusOfSpecifiedBoard(computerBoard);

                        // Check if computer would block player at position
                        if ((player == Cell.X && testedStatus == GameStatus.X_WON) || (player == Cell.O && testedStatus == GameStatus.O_WON)) {
                            weightedBoard[row][col] += 100;
                        }

                        computerBoard[row][col] = Cell.EMPTY;

                        // Trying to make lines
                        weightedBoard[row][col] += findCombosAtCell(computerBoard, row, col);
                    }
                    // If not empty make -1 so that it should never be picked
                    else {
                        weightedBoard[row][col] = -1;
                    }
                }
            }

            // Search the weighted board for the best move
            for (int row = 0; row < lastBoardSize; row++) {
                for (int col = 0; col < lastBoardSize; col++) {
                    // If find a better move remember it instead.
                    if (weightedBoard[row][col] >= bestMoveValue) {
                        bestMoveValue = weightedBoard[row][col];

                        bestValidMoveRow = row;
                        bestValidMoveCol = col;
                    }
                }
            }

            // Check there is a best move on the board, if not don't make a move
            if (bestValidMoveRow > -1 && bestValidMoveCol > -1) {
                select(bestValidMoveRow, bestValidMoveCol);
            }
        }
    } // End of playComputer Method

    /**
     * Private helper method.
     * Finds how many cells are the target symbol in a direct line on the cardinal directions of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @return Total number of cells that in direct lines on cardinal directions.
     */
    private int findCombosAtCell(Cell[][] boardBeingChecked, int startingRow, int startingCol) {
        int comboValue = 0;

        // Handle any exceptions here because only computer deals with these variables.
        try {
            // Check for computer player symbol combos at cell
            comboValue += findComboNOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboNEOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboEOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboSEOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboSOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboSWOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboWOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);
            comboValue += findComboNWOfCell(boardBeingChecked, startingRow, startingCol, computerPlayer);


            // Check for player symbol combos at cell
            comboValue += findComboNOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboNEOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboEOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboSEOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboSOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboSWOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboWOfCell(boardBeingChecked, startingRow, startingCol, player);
            comboValue += findComboNWOfCell(boardBeingChecked, startingRow, startingCol, player);
        } catch (IllegalArgumentException exc) {
            // Should never have an exception as this is hard coded.
            System.out.println(exc);
        }

        return comboValue;
    }

    /**
     * Private helper method for code reuse.
     * Checks parameters for combo methods.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private void checkComboParameters(Cell[][] boardBeingChecked, int startingRow, int startingCol) {
        if (boardBeingChecked == null) {
            throw new IllegalArgumentException("Board being checked must not be null.");
        }
        if (startingRow < 0 || startingRow >= boardBeingChecked.length) {
            throw new IllegalArgumentException("Starting row must be on the board being checked.");
        }
        if (startingCol < 0 || startingCol >= boardBeingChecked[0].length) {
            throw new IllegalArgumentException("Starting column must be on the board being checked.");
        }
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol north of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboNOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingRow - comboLength - 1 >= 0) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow - comboLength - 1][startingCol] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol north-east of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboNEOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingRow - comboLength - 1 >= 0 && startingCol + comboLength + 1 < lastBoardSize) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow - comboLength - 1][startingCol + comboLength + 1] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol east of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboEOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingCol + comboLength + 1 < lastBoardSize) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow][startingCol + comboLength + 1] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol south-east of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboSEOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingRow + comboLength + 1 < lastBoardSize && startingCol + comboLength + 1 < lastBoardSize) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow + comboLength + 1][startingCol + comboLength + 1] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol south of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboSOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingRow + comboLength + 1 < lastBoardSize) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow + comboLength + 1][startingCol] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol south-west of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboSWOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingRow + comboLength + 1 < lastBoardSize && startingCol - comboLength - 1 >= 0) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow + comboLength + 1][startingCol - comboLength - 1] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol west of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboWOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingCol - comboLength - 1 >= 0) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow][startingCol - comboLength - 1] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /**
     * Private helper method.
     * Finds how many cells are the target symbol north-west of the starting position and in bounds.
     *
     * @param boardBeingChecked The board being checked currently.
     * @param startingRow       Row of the cell the method is checking from.
     * @param startingCol       Column of the cell the method is checking from.
     * @param targetSymbol      Player's symbol being counted.
     * @return Number of cells that are the target symbol going north.
     * @throws IllegalArgumentException If board being checked is null,
     *                                  Starting row is not on board being checked.
     *                                  Starting column is not on board being checked.
     */
    private int findComboNWOfCell(Cell[][] boardBeingChecked, int startingRow, int startingCol, Cell targetSymbol) {
        // Check if the parameters are legal
        checkComboParameters(boardBeingChecked, startingRow, startingCol);

        int comboLength = 0;

        // While row is in bounds
        while (startingRow - comboLength - 1 >= 0 && startingCol - comboLength - 1 >= 0) {
            // If not target symbol, return the combo length
            if (boardBeingChecked[startingRow - comboLength - 1][startingCol - comboLength - 1] != targetSymbol) {
                return comboLength;
            }

            // Increment the combo length
            comboLength++;
        }

        // Return the combo length
        return comboLength;
    }

    /************************************************************/
    /* checkRows Method */

    /**
     * checkRows checks across each row in the game board for a winner of user inputted amount of X's or O's in a row.
     * Returns the status of the game.
     *
     * @param boardBeingChecked The board being checked currently.
     * @return GameStatus current status of the game.
     */
    private GameStatus checkRows(Cell[][] boardBeingChecked) {
        xCount = 0;
        oCount = 0;

        for (int row = 0; row < lastBoardSize; row++) {
            for (int column = 0; column < lastBoardSize; column++) {
                if (boardBeingChecked[row][column] == Cell.X) {
                    xCount++;
                    oCount = 0;
                    if (xCount == numToWin) {
                        return GameStatus.X_WON;
                    }
                }

                if (boardBeingChecked[row][column] == Cell.O) {
                    oCount++;
                    xCount = 0;
                    if (oCount == numToWin) {
                        return GameStatus.O_WON;
                    }
                }

                if (boardBeingChecked[row][column] == Cell.EMPTY) {
                    xCount = 0;
                    oCount = 0;
                }
            }

            // Reset counters
            xCount = 0;
            oCount = 0;
        }

        return GameStatus.IN_PROGRESS;
    }

    /**
     * checkColumns checks down each column in the game board for a winner of user inputted amount of X's or O's in a row.
     * Returns the status of the game.
     *
     * @param boardBeingChecked The board being checked currently.
     * @return GameStatus current status of the game.
     */
    private GameStatus checkColumns(Cell[][] boardBeingChecked) {
        int xCount = 0;
        int oCount = 0;

        for (int column = 0; column < lastBoardSize; column++) {
            for (int row = 0; row < lastBoardSize; row++) {
                if (boardBeingChecked[row][column] == Cell.X) {
                    xCount++;
                    oCount = 0;
                    if (xCount == numToWin) {
                        return GameStatus.X_WON;
                    }
                }

                if (boardBeingChecked[row][column] == Cell.O) {
                    oCount++;
                    xCount = 0;
                    if (oCount == numToWin) {
                        return GameStatus.O_WON;
                    }
                }

                if (boardBeingChecked[row][column] == Cell.EMPTY) {
                    xCount = 0;
                    oCount = 0;
                }
            }

            // Reset counters
            xCount = 0;
            oCount = 0;
        }

        return GameStatus.IN_PROGRESS;
    }

    /**
     * checkDiagonalNWSE checks down each NWSE diagonal in the game board for a winner of user inputted amount of X's or
     * O's in a row. Returns the status of the game.
     *
     * @param boardBeingChecked The board being checked currently.
     * @return GameStatus current status of the game.
     */
    private GameStatus checkDiagonalNWSE(Cell[][] boardBeingChecked) {
        int xCount = 0;
        int oCount = 0;

        int row, col;
        col = 0;

        // Outer loop adds to row
        for (row = 0; row < lastBoardSize; row++) {
            // Inner loop to check diagonal
            for (int diag = 0; diag < lastBoardSize - row; diag++) {
                // Check for X win
                if (boardBeingChecked[row + diag][col + diag] == Cell.X) {
                    xCount++;
                    oCount = 0;
                    if (xCount == numToWin) {
                        return GameStatus.X_WON;
                    }
                }
                // Check for O win
                if (boardBeingChecked[row + diag][col + diag] == Cell.O) {
                    oCount++;
                    xCount = 0;
                    if (oCount == numToWin) {
                        return GameStatus.O_WON;
                    }
                }

                if (boardBeingChecked[row + diag][col + diag] == Cell.EMPTY) {
                    xCount = 0;
                    oCount = 0;
                }
            }

            // Reset counters
            xCount = 0;
            oCount = 0;
        }

        row = 0;

        // Outer loop adds to column
        for (col = 0; col < lastBoardSize; col++) {
            // Inner loop to check diagonal
            for (int diag = 0; diag < lastBoardSize - col; diag++) {
                // Check for X win
                if (boardBeingChecked[row + diag][col + diag] == Cell.X) {
                    xCount++;
                    oCount = 0;
                    if (xCount == numToWin) {
                        return GameStatus.X_WON;
                    }
                }
                // Check for O win
                if (boardBeingChecked[row + diag][col + diag] == Cell.O) {
                    oCount++;
                    xCount = 0;
                    if (oCount == numToWin) {
                        return GameStatus.O_WON;
                    }
                }

                if (boardBeingChecked[row + diag][col + diag] == Cell.EMPTY) {
                    xCount = 0;
                    oCount = 0;
                }

            }

            // Reset counters
            xCount = 0;
            oCount = 0;
        }

        return GameStatus.IN_PROGRESS;
    }

    /**
     * checkDiagonalNESW checks down each NESW diagonal in the game board for a winner of user inputted amount
     * of X's or O's in a row. Returns the status of the game.
     *
     * @param boardBeingChecked The board being checked currently.
     * @return GameStatus current status of the game.
     */
    private GameStatus checkDiagonalNESW(Cell[][] boardBeingChecked) {
        int xCount = 0;
        int oCount = 0;
        int row, col;

        col = lastBoardSize - 1; // Start at the right side of the board

        // Check NE-SW diagonals going down the row (from right most side)
        for (row = 0; row <= lastBoardSize - 1; row++) {
            // The conditions here make sure that it can not go out of bounds of the array
            // (So it won't go negative from [col-diag] or exceeding bounds from [row+diag])
            for (int diag = 0; (col >= diag) && (row + diag < lastBoardSize); diag++) {
                // Check for x win
                if (boardBeingChecked[row + diag][col - diag] == Cell.X) {
                    xCount++;
                    oCount = 0;
                    if (xCount == numToWin) {
                        return GameStatus.X_WON;
                    }
                }
                // Check for O win
                if (boardBeingChecked[row + diag][col - diag] == Cell.O) {
                    oCount++;
                    xCount = 0;
                    if (oCount == numToWin) {
                        return GameStatus.O_WON;
                    }
                }

                if (boardBeingChecked[row + diag][col - diag] == Cell.EMPTY) {
                    xCount = 0;
                    oCount = 0;
                }
            }

            // Reset counters
            xCount = 0;
            oCount = 0;
        }

        row = 0; // Set row to 0 to start at top row
        // Check NE-SW diagonals going along the columns (right to left, starting at top right
        // of board)
        for (col = lastBoardSize - 1; col >= 0; col--) {
            // Check the NE-SW diagonals
            for (int diag = 0; col >= diag; diag++) {
                // Check for X win
                if (boardBeingChecked[row + diag][col - diag] == Cell.X) {
                    xCount++;
                    oCount = 0;
                    if (xCount == numToWin) {
                        return GameStatus.X_WON;
                    }
                }
                // Check for O win
                if (boardBeingChecked[row + diag][col - diag] == Cell.O) {
                    oCount++;
                    xCount = 0;
                    if (oCount == numToWin) {
                        return GameStatus.O_WON;
                    }
                }

                if (boardBeingChecked[row + diag][col - diag] == Cell.EMPTY) {
                    xCount = 0;
                    oCount = 0;
                }
            }

            // Reset counters
            xCount = 0;
            oCount = 0;
        }

        return GameStatus.IN_PROGRESS;
    }

    /************************************************************/
    /* Undo Method */

    /**
     * Undo method undoes the last player move made, turning the cell empty and changing back to the correct player.
     *
     * @throws IllegalArgumentException if the board is empty and cannot undo any further moves
     *                                  or trying to remove computer player's first move.
     */
    public void undo() {
        // Number of points being removed
        int numOfPointsBeingRemoved = 0;

        // Find the last element in the array list
        int lastElement = point.size() - 1;

        // If board is empty, cannot undo further
        if (lastElement == -1) {
            throw new IllegalArgumentException("Can not undo further.");
        } else {
            // If computer is not playing and press undo
            if (!computerIsPlaying) {
                // Changes who is the current player after the undo
                if (currentPlayer == Cell.O) {
                    currentPlayer = Cell.X;
                } else {
                    currentPlayer = Cell.O;
                }

                numOfPointsBeingRemoved = 1;
            }
            // don't want to let them undo the computer's first move
            else if (computerIsPlaying && startPlayer == computerPlayer && numTurns == 1) {
                throw new IllegalArgumentException("Can not undo the computer's first turn." + "\nReset if you want to clear the board.");
            }
            // if play computer is enabled, it will remove the last 2 moves
            else if (computerIsPlaying && currentPlayer == player) {
                currentPlayer = Cell.X;
                numOfPointsBeingRemoved = 2;
            }
            // For loop to remove the last element in point array list and clear the icon
            for (int i = 0; i < numOfPointsBeingRemoved && lastElement >= 0; i++) {
                // Set the cell of the last point in the array list to empty
                board[(int) point.get(lastElement).getX()][(int) point.get(lastElement).getY()] = Cell.EMPTY;

                // Remove the last element from the array list
                point.remove(lastElement);
                // Update lastElement
                lastElement = point.size() - 1;
                // decrement turn counter
                numTurns--;
            }
        }
    }
} // End of class SuperTicTacToeGame


