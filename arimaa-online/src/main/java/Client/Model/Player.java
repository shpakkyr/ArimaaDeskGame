package Client.Model;

public class Player {
    private final int id;
    private final PlayingSide playingSide;
    private String playerName;

    public Player(int id, PlayingSide playingSide, String playerName) {
        this.id = id;
        this.playingSide = playingSide;
        this.playerName = playerName;
    }

    public int getId() {
        return id;
    }

    public PlayingSide getPlayingSide() {
        return playingSide;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
