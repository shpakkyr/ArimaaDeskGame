package Client.Model;

public class Dog extends Troop{
    public Dog(PlayingSide playingSide, int currentRow, int currentCol) {
        super(TroopType.DOG, playingSide, currentRow, currentCol);
    }
}
