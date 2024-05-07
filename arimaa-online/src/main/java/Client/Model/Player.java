package Client.Model;

public class Player {
    private final int playerId;
    private final PlayingSide playingSide;
    private final String playerName;
    private final boolean isComputer;

    public Player(int playerId, String playerName, boolean isComputer) {
        this.playerId = playerId;
        this.playerName = playerName;
        if(playerId == 0) {
            this.playingSide = PlayingSide.GOLD;
        } else {
            this.playingSide = PlayingSide.SILVER;
        }
        this.isComputer = isComputer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public PlayingSide getPlayingSide() {
        return playingSide;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isComputer() {
        return isComputer;
    }
}
