package Client.Model;

/**
 * Interface for game event listeners.
 * Implementations of this interface will handle game events such as the number of moves left and the end of the game.
 */
public interface GameListener {

    /**
     * Called when the number of moves left changes.
     *
     * @param movesLeft The updated number of moves left.
     */
    void onMovesLeftChanged(int movesLeft);

    /**
     * Called when the game ends.
     *
     * @param winner The player who won the game.
     */
    void onGameEnded(Player winner);
}
