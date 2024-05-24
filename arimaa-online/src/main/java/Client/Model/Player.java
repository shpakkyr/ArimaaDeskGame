package Client.Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a player in the game.
 * Implements Serializable to allow the player data to be saved and loaded.
 */
public class Player implements Serializable {
    private final int playerId;
    private final PlayingSide playingSide;
    private final String playerName;
    private final boolean isComputer;

    /**
     * Constructs a Player object with the specified ID, name, and computer status.
     *
     * @param playerId   The ID of the player.
     * @param playerName The name of the player.
     * @param isComputer Whether the player is controlled by a computer.
     */
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

    /**
     * Compares this player to the specified object. The result is true if and only if the argument is not null
     * and is a Player object that has the same ID, computer status, and playing side as this player.
     *
     * @param obj The object to compare this Player against.
     * @return true if the given object represents a Player equivalent to this player, false otherwise.
     */
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

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", playingSide=" + playingSide +
                ", playerName='" + playerName + '\'' +
                ", isComputer=" + isComputer +
                '}';
    }
}
