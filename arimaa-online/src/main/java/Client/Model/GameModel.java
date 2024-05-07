package Client.Model;

public class GameModel {
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Player enemyPlayer;
    private Player winner;
    private int phase;
    private int movesLeft;
    private boolean isGameFinished;
    private Board board;

    public GameModel(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.enemyPlayer = player2;
        this.phase = 1;
    }

    public void switchTurn(){
        phase++;
        Player newEnemyPlayer = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = newEnemyPlayer;
        movesLeft = 4;
    }

    public void decrementMovesLeft(int number) {
        movesLeft -= number;
    }

    public void checkWinner() {
        boolean player1State = board.isWinner(player1, player2);
        boolean player2State = board.isWinner(player1, player2);

        if (player1State || player2State) {
            isGameFinished = true;
            winner = player1State ? player1 : player2;
        }
    }
}
