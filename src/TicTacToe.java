import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private final char[][] board;
    private char currentPlayerMark;
    private final Random random;
    private final Scanner scanner;

    public TicTacToe() {
        this.board = new char[3][3];
        this.currentPlayerMark = 'X'; // X starts the game
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        initializeBoard();
    }

    // Initialize or reset the board
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    // Print the current board state
    public void printBoard() {
        System.out.println("Board:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(" " + board[i][j] + " ");
                if (j < board[i].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < board.length - 1) {
                System.out.println("---+---+---");
            }
        }
    }

    // Check if a move is valid
// Check if a move is valid and place the mark
    private boolean placeMark(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-') {
            board[row][col] = currentPlayerMark;
            return false;
        }
        return true;
    }

    // Check if the current player has won
    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2]) || // rows
                    checkRowCol(board[0][i], board[1][i], board[2][i])) { // columns
                return true;
            }
        }
        // diagonals
        return checkRowCol(board[0][0], board[1][1], board[2][2]) ||
                checkRowCol(board[0][2], board[1][1], board[2][0]);
    }

    // Check if all three values are the same (and not empty) indicating a win
    private boolean checkRowCol(char c1, char c2, char c3) {
        return (c1 != '-') && (c1 == c2) && (c2 == c3);
    }

    // Check for a draw (no spaces left)
    private boolean checkForDraw() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    // Switch the current player
    private void switchPlayer() {
        currentPlayerMark = (currentPlayerMark == 'X') ? 'O' : 'X';
    }

    // The input of the player choice
    private void turnOfPlayer() {
        System.out.println("Player " + currentPlayerMark + "'s turn:");
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter row and column numbers (1-3): ");
            int row = -1, col = -1;
            if (scanner.hasNextInt()) {
                row = scanner.nextInt();
                if (scanner.hasNextInt()) {
                    col = scanner.nextInt();
                    if (row >= 1 && row <= 3 && col >= 1 && col <= 3) {
                        // Adjust for 0-based indexing after validating input range
                        row -= 1;
                        col -= 1;
                        // Now check if the position is not occupied
                        if (placeMark(row, col)) {
                            validInput = true; // The position was valid, and the mark was successfully placed.
                        } else {
                            System.out.println("This position is already occupied or out of bounds, try again.");
                        }
                    } else {
                        System.out.println("Invalid input: row and column numbers must be between 1 and 3.");
                        // Consume any extra input (newline characters, etc.) to reset for next input
                        if (scanner.hasNextLine()) scanner.nextLine();
                    }
                } else {
                    System.out.println("Invalid input: you must enter two numbers.");
                    // Ensure scanner is clear for next input attempt
                    scanner.nextLine();
                }
            } else {
                System.out.println("Invalid input: you must enter numbers.");
                scanner.nextLine(); // Clear invalid input before retrying
            }
        }
    }

    // The AI's turn
    // AI Move with strategy
    private void aiMove() {
        int row;
        int col;
        // Check if AI can win in the next move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = currentPlayerMark; // Try move
                    if (checkForWin()) {
                        System.out.println("AI placed " + currentPlayerMark + " in position (" + (i + 1) + "," + (j + 1) + ")");
                        return;
                    }
                    board[i][j] = '-'; // Undo move
                }
            }
        }

        // Block opponent's winning move
        char opponentMark = (currentPlayerMark == 'X') ? 'O' : 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = opponentMark; // Try opponent's move
                    if (checkForWin()) {
                        board[i][j] = currentPlayerMark; // Block
                        System.out.println("AI placed " + currentPlayerMark + " in position (" + (i + 1) + "," + (j + 1) + ")");
                        return;
                    }
                    board[i][j] = '-'; // Undo move
                }
            }
        }

        // Take center if available
        if (board[1][1] == '-') {
            board[1][1] = currentPlayerMark;
            System.out.println("AI placed " + currentPlayerMark + " in position (2,2)");
            return;
        }

        // Take any corner
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == '-') {
                board[corner[0]][corner[1]] = currentPlayerMark;
                System.out.println("AI placed " + currentPlayerMark + " in position (" + (corner[0] + 1) + "," + (corner[1] + 1) + ")");
                return;
            }
        }

        // Fallback to a random move if no other move is possible
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (placeMark(row, col));
        System.out.println("AI placed " + currentPlayerMark + " in position (" + (row + 1) + "," + (col + 1) + ")");
    }


    // Single-player game loop
    private void singlePlayerGame() {
        boolean playerTurn = true; // Player starts
        while (true) {
            printBoard();
            if (playerTurn) {
                turnOfPlayer();
            } else {

                aiMove();
            }
            if (checkForWin()) {
                printBoard();
                System.out.println("Player " + currentPlayerMark + " wins!");
                break;
            } else if (checkForDraw()) {
                printBoard();
                System.out.println("The game is a draw!");
                break;
            }
            switchPlayer();
            playerTurn = !playerTurn; // Switch turns
        }
    }

    // Two-player game loop
    private void twoPlayerGame() {
        while (true) {
            printBoard();
            turnOfPlayer();
            if (checkForWin()) {
                printBoard();
                System.out.println("Player " + currentPlayerMark + " wins!");
                break;
            } else if (checkForDraw()) {
                printBoard();
                System.out.println("The game is a draw!");
                break;
            }
            switchPlayer();
        }
    }

    // Main menu and game launcher
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tic Tac Toe!");

        while (true) {
            System.out.println("1. Single Player\n2. Two Player\n3. Exit");
            System.out.print("Choose an option: ");

            // Check if the next input is an integer
            while (!scanner.hasNextInt()) {
                scanner.next(); // Consume the non-integer input
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                System.out.println("1. Single Player\n2. Two Player\n3. Exit");
                System.out.print("Choose an option: ");
            }

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    game.singlePlayerGame();
                    break;
                case 2:
                    game.twoPlayerGame();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close(); // Close the scanner before exiting
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
            game.initializeBoard(); // Reset the board for a new game
        }
    }

}