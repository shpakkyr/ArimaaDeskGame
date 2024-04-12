package Client.Model;

public class Rabbit extends Troop{
    public Rabbit(PlayingSide playingSide, int currentRow, int currentCol) {
        super(TroopType.RABBIT, playingSide, currentRow, currentCol);
    }
}
