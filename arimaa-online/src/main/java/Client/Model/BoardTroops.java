package Client.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final ArrayList<Troop> troopArray;

    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, ArrayList<Troop> troopArray) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.troopArray = troopArray;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        //TODO
        return this;
    }
    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        //TODO
        return this;
    }

    public BoardTroops removeTroop(BoardPos target) {
        //TODO
        return this;
    }
}
