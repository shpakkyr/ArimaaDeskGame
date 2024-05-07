package Client.Model;

import java.util.ArrayList;
import java.util.List;

public class Troop {
    private final TroopType type;
    private final Player player;

    public Troop(TroopType type, Player player) {
        this.type = type;
        this.player = player;
    }

    public TroopType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Directions> troopValidDirections() {
        ArrayList<Directions> troopDirections =  new ArrayList<>(List.of(Directions.NORTH, Directions.SOUTH, Directions.LEFT, Directions.RIGHT));
        if(player.getPlayingSide() == PlayingSide.GOLD) {
            troopDirections.remove(Directions.SOUTH);
        } else {
            troopDirections.remove(Directions.NORTH);
        }
        return troopDirections;
    }
}
