package Client.Model;

import java.util.ArrayList;

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

    public GameModel(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.enemyPlayer = player2;
        this.phase = 1;
        isGameFinished = false;
    }

    public void switchTurn() {
        Player newEnemyPlayer = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = newEnemyPlayer;
        movesLeft = 4;
    }

    public void decrementMovesLeft(int number) {
        movesLeft -= number;
        gameListener.onMovesLeftChanged(movesLeft);
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public void endGameGiveUp() {
        setWinner(getEnemyPlayer());
        isGameFinished = true;
        gameListener.onGameEnded(getEnemyPlayer());
    }

    public void incrementPhase() {
        phase++;
        switchTurn();
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

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.enemyPlayer = player2;
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    public void checkWinning() {
        boolean player1won = board.isWinner(player1, player2);
        boolean player2won = board.isWinner(player2, player1);
        if (player1won || player2won) {
            winner = player1won ? player1 : player2;
            isGameFinished = true;
            gameListener.onGameEnded(winner);
        }
    }
    public GameState saveState(long remainingTime, boolean vsComputer) {
        return new GameState(player1, player2, currentPlayer, enemyPlayer, winner, phase, movesLeft, isGameFinished, board.copyBoard(), remainingTime, vsComputer);
    }

    public void loadState(ArrayList<GameState> gameState) {
        this.player1 = gameState.getLast().player1;
        this.player2 = gameState.getLast().player2;
        this.currentPlayer = gameState.getLast().currentPlayer;
        this.enemyPlayer = gameState.getLast().enemyPlayer;
        this.winner = gameState.getLast().winner;
        this.phase = gameState.getLast().phase;
        this.movesLeft = gameState.getLast().movesLeft;
        this.isGameFinished = gameState.getLast().isGameFinished;
    }

    public void loadReplayState(ArrayList<GameState> gameState) {
        this.player1 = gameState.getFirst().player1;
        this.player2 = gameState.getFirst().player2;
        this.currentPlayer = gameState.getFirst().currentPlayer;
        this.enemyPlayer = gameState.getFirst().enemyPlayer;
        this.winner = gameState.getFirst().winner;
        this.phase = gameState.getFirst().phase;
        this.movesLeft = gameState.getFirst().movesLeft;
        this.isGameFinished = gameState.getFirst().isGameFinished;
    }
}