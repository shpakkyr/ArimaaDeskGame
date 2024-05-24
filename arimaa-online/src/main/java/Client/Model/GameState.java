package Client.Model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents the state of the game at a particular point in time.
 * Implements Serializable to allow the game state to be saved and loaded.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Player player1;
    public final Player player2;
    public final Player currentPlayer;
    public final Player enemyPlayer;
    public final Player winner;
    public final int phase;
    public final int movesLeft;
    public final boolean isGameFinished;
    public final String[][] boardState;
    public final long remainingTime;
    public final boolean vsComputer;

    /**
     * Constructs a GameState object with the specified parameters.
     *
     * @param player1       The first player.
     * @param player2       The second player.
     * @param currentPlayer The current player.
     * @param enemyPlayer   The enemy player.
     * @param winner        The player who won the game.
     * @param phase         The current phase of the game.
     * @param movesLeft     The number of moves left.
     * @param isGameFinished Whether the game is finished.
     * @param boardState    The current state of the board.
     * @param remainingTime The remaining time for the game.
     * @param vsComputer    Whether the game is against the computer.
     */
    public GameState(Player player1, Player player2, Player currentPlayer, Player enemyPlayer, Player winner, int phase, int movesLeft, boolean isGameFinished, String[][] boardState, long remainingTime, boolean vsComputer) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        this.enemyPlayer = enemyPlayer;
        this.winner = winner;
        this.phase = phase;
        this.movesLeft = movesLeft;
        this.isGameFinished = isGameFinished;
        this.boardState = boardState;
        this.remainingTime = remainingTime;
        this.vsComputer = vsComputer;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", currentPlayer=" + currentPlayer +
                ", enemyPlayer=" + enemyPlayer +
                ", winner=" + winner +
                ", phase=" + phase +
                ", movesLeft=" + movesLeft +
                ", isGameFinished=" + isGameFinished +
                ", boardState=" + Arrays.toString(boardState) +
                ", remainingTime=" + remainingTime +
                ", vsComputer=" + vsComputer +
                '}';
    }
}
