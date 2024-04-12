package Client.Model;

public class Cat extends Troop{
    public Cat(PlayingSide playingSide, int currentRow, int currentCol) {
        super(TroopType.CAT, playingSide, currentRow, currentCol);
    }
}
