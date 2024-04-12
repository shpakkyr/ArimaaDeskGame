package Client.Model;

public class Troop {
    private TroopType type;
    private Offset2D offset;

    public Troop(TroopType type, Offset2D offset) {
        this.type = type;
        this.offset = offset;
    }
}
