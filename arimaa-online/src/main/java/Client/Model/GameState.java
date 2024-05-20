package Client.Model;

import java.io.Serializable;

import java.io.Serializable;

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
}
