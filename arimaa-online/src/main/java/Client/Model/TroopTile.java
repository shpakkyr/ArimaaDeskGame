package Client.Model;

/*
This class implements tile where the troop will stand.
The tile will always remember if some troop is on it's tile or not.
*/
public class TroopTile implements Tile{
    private Troop troop;

    //return false because troop is on this tile
    @Override
    public boolean canStepOn() {
        return false;
    }

    //return true because troop is already on this tile
    @Override
    public boolean hasTroop() {
        return true;
    }
}
