package Client.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a tile on the game board, which can contain a troop and a player.
 */
public class Tile {
    private Troop troop;
    private Player player;

    /**
     * Constructs a Tile object with the specified troop.
     *
     * @param troop The troop to be placed on the tile.
     */
    public Tile(Troop troop) {
        this.troop = troop;
    }

    public Troop getTroop() {
        return troop;
    }

    public Player getPlayer() {
        return player;
    }

    public void setTroop(Troop troop) {
        this.troop = troop;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the valid directions the troop can move, based on its type and the player's side.
     *
     * @return A list of valid directions for the troop.
     */
    public List<Directions> troopValidDirections(boolean isSilverBottom) {
        List<Directions> troopDirections =  new LinkedList<>(List.of(Directions.NORTH, Directions.SOUTH, Directions.LEFT, Directions.RIGHT));
        if(player.getPlayingSide() == PlayingSide.GOLD && troop.type() == TroopType.RABBIT && !isSilverBottom) {
            troopDirections.remove(Directions.SOUTH);
        } else if (player.getPlayingSide() == PlayingSide.SILVER && troop.type() == TroopType.RABBIT && !isSilverBottom) {
            troopDirections.remove(Directions.NORTH);
        } else if (player.getPlayingSide() == PlayingSide.SILVER && troop.type() == TroopType.RABBIT && isSilverBottom)
            troopDirections.remove(Directions.SOUTH);
        return troopDirections;
    }
}
