package Client.Model;

public class Player {
    private final int playerId;
    private final String playerName;
    private final PlayingSide playingSide;

    public Player(int playerId, String playerName, PlayingSide playingSide) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playingSide = playingSide;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayingSide getPlayingSide() {
        return playingSide;
    }
}
