package Client.Model;

import java.io.IOException;
import java.util.ArrayList;
import Server.*;

/**
 * Represents the model for the game, containing the game logic and state.
 */
public class GameModel {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player enemyPlayer;
    private Player winner;
    private int phase;
    private int movesLeft;
    private boolean isGameFinished;
    private Board board;
    private GameListener gameListener;
    private Client client;

    /**
     * Default board configuration for a new game.
     */
    public static final String[][] DEFAULT_BOARD = {
            {"r", "r", "r", "r", "r", "r", "r", "r"},
            {"e", "m", "h", "h", "d", "d", "c", "c"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"E", "M", "H", "H", "D", "D", "C", "C"},
            {"R", "R", "R", "R", "R", "R", "R", "R"}
    };

    /**
     * Default board configuration for a new game if player plays as silver player in online multiplayer.
     */
    public static final String[][] DEFAULT_BOARD_ROTATED = {
            {"R", "R", "R", "R", "R", "R", "R", "R"},
            {"C", "C", "D", "D", "H", "H", "M", "E"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"c", "c", "d", "d", "h", "h", "m", "e"},
            {"r", "r", "r", "r", "r", "r", "r", "r"}
    };

    /**
     * Constructs a GameModel with the specified players.
     *
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public GameModel(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.enemyPlayer = player2;
        this.phase = 1;
        isGameFinished = false;
    }

    /**
     * Switches the turn between the current player and the enemy player.
     */
    public void switchTurn() {
        Player newEnemyPlayer = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = newEnemyPlayer;
        movesLeft = 4;
    }

    /**
     * Decreases the number of moves left by the specified number and notifies the listener.
     *
     * @param number The number by which to decrement the moves left.
     */
    public void decrementMovesLeft(int number) {
        movesLeft -= number;
        gameListener.onMovesLeftChanged(movesLeft);
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner The player who won the game.
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Ends the game by making the current player give up.
     */
    public void endGameGiveUp() {
        setWinner(getEnemyPlayer());
        isGameFinished = true;
        if (client != null) {
            GameState gameState = saveState(0, false);
            try {
                client.sendObjectToEnemy(gameState);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        gameListener.onGameEnded(getEnemyPlayer());
    }

    /**
     * Increments the game phase and switches the turn.
     */
    public void incrementPhase() {
        phase++;
        switchTurn();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public int getPhase() {
        return phase;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public Player getWinner() {
        return winner;
    }

    /**
     * Checks if the game is finished.
     *
     * @return True if the game is finished, otherwise false.
     */
    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setCurrentPlayer(Player player1, Player player2){

        this.currentPlayer = player1;
        this.enemyPlayer = player2;
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    /**
     * Checks if there is a winner and updates the game state accordingly.
     */
    public void checkWinning() {
        boolean player1won = board.isWinner(player1, player2, 1);
        boolean player2won = board.isWinner(player2, player1, 1);
        if (player1won || player2won) {
            winner = player1won ? player1 : player2;
            isGameFinished = true;
            gameListener.onGameEnded(winner);
        }
    }

    /**
     * Checks if there is a winner and updates the game state accordingly if tha game is online.
     */
    public void checkWinningOnline() {
        boolean player1won = board.isWinner(player1, player2, getCurrentPlayer().getPlayerId());
        boolean player2won = board.isWinner(player2, player1, getCurrentPlayer().getPlayerId());
        if (player1won || player2won) {
            winner = player1won ? player1 : player2;
            isGameFinished = true;
            if (client != null) {
                GameState gameState = saveState(0, false);
                try {
                    client.sendObjectToEnemy(gameState);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            gameListener.onGameEnded(winner);
        }
    }

    /**
     * Saves the current game state.
     *
     * @param remainingTime The remaining time for the game.
     * @param vsComputer    Whether the game is against the computer.
     * @return The saved game state.
     */
    public GameState saveState(long remainingTime, boolean vsComputer) {
        return new GameState(player1, player2, currentPlayer, enemyPlayer, winner, phase, movesLeft, isGameFinished, board.copyBoard(), remainingTime, vsComputer);
    }

    /**
     * Loads the game state from a list of game states.
     *
     * @param gameState The deque of game states.
     */
    public void loadState(ArrayList<GameState> gameState) {
        this.player1 = gameState.get(gameState.size()-1).player1;
        this.player2 = gameState.get(gameState.size()-1).player2;
        this.currentPlayer = gameState.get(gameState.size()-1).currentPlayer;
        this.enemyPlayer = gameState.get(gameState.size()-1).enemyPlayer;
        this.winner = gameState.get(gameState.size()-1).winner;
        this.phase = gameState.get(gameState.size()-1).phase;
        this.movesLeft = gameState.get(gameState.size()-1).movesLeft;
        this.isGameFinished = gameState.get(gameState.size()-1).isGameFinished;
    }

    /**
     * Updates the game state for a player in online multiplayer, when the move was made by another player.
     *
     * @param gameState The game state which should be assigned.
     */
    public void updateGameState(GameState gameState) {
        this.currentPlayer = gameState.currentPlayer;
        this.enemyPlayer = gameState.enemyPlayer;
        this.winner = gameState.winner;
        this.phase = gameState.phase;
        this.movesLeft = gameState.movesLeft;
        this.isGameFinished = gameState.isGameFinished;
    }

    /**
     * Loads the replay state from a list of game states.
     *
     * @param gameState The deque of game states.
     */
    public void loadReplayState(ArrayList<GameState> gameState) {
        this.player1 = gameState.get(0).player1;
        this.player2 = gameState.get(0).player2;
        this.currentPlayer = gameState.get(0).currentPlayer;
        this.enemyPlayer = gameState.get(0).enemyPlayer;
        this.winner = gameState.get(0).winner;
        this.phase = gameState.get(0).phase;
        this.movesLeft = gameState.get(0).movesLeft;
        this.isGameFinished = gameState.get(0).isGameFinished;
    }
}