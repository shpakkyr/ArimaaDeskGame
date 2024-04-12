package Client.Model;

public class Troop {
    private final TroopType type;
    private final PlayingSide playingSide;
    private int currentRow;
    private int currentCol;

    public Troop(TroopType type, PlayingSide playingSide, int currentRow, int currentCol) {
        this.type = type;
        this.playingSide = playingSide;
        this.currentRow = currentRow;
        this.currentCol = currentCol;
    }

    private boolean isMoveValid(int row, int col){
        //TODO
        return true;
    }
}
