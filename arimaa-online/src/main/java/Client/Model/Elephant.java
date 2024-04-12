package Client.Model;

public class Elephant extends Troop{
    public Elephant(PlayingSide playingSide, int currentRow, int currentCol) {
        super(TroopType.ELEPHANT, playingSide, currentRow, currentCol);
    }
}
