package Client.Model;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private Troop troop;
    private Player player;

    public Tile(Troop troop) {
        this.troop = troop;
    }

    public Tile(Troop troop, Player player) {
        this.troop = troop;
        this.player = player;
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

    public ArrayList<Directions> troopValidDirections() {
        ArrayList<Directions> troopDirections =  new ArrayList<>(List.of(Directions.NORTH, Directions.SOUTH, Directions.LEFT, Directions.RIGHT));
        if(player.getPlayingSide() == PlayingSide.GOLD && troop.getType() == TroopType.RABBIT) {
            troopDirections.remove(Directions.NORTH);
        } else if (player.getPlayingSide() == PlayingSide.SILVER && troop.getType() == TroopType.RABBIT) {
            troopDirections.remove(Directions.SOUTH);
        }
        return troopDirections;
    }
}
