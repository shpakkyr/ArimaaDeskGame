package Client.Model;

import java.util.Objects;

public class Player {
    private final int playerId;
    private final PlayingSide playingSide;
    private final String playerName;
    private final boolean isComputer;

    public Player(int playerId, String playerName, boolean isComputer) {
        this.playerId = playerId;
        this.playerName = playerName;
        if(playerId == 1) {
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

    public String getPlayingSideString() {
        return playingSide.getPlayingSide();
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isComputer() {
        return isComputer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player other = (Player) obj;
        return playerId == other.playerId &&
                isComputer == other.isComputer &&
                playingSide == other.playingSide;
    }
}
