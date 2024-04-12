package Client.Model;

public class Horse extends Troop{
    public Horse(PlayingSide playingSide, int currentRow, int currentCol) {
        super(TroopType.HORSE, playingSide, currentRow, currentCol);
    }
}
