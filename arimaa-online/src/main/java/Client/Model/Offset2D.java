package Client.Model;

import java.util.ArrayList;
import java.util.List;

public class Offset2D {
    private int row;
    private int column;

    public Offset2D(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static final ArrayList<Offset2D> TRAP_OFFSET = new ArrayList<>(
            List.of(
                    new Offset2D(2, 2),
                    new Offset2D(2, 5),
                    new Offset2D(5, 2),
                    new Offset2D(5, 5)
            )
    );

    public static final ArrayList<Offset2D> GOLD_WINNING_CONDITION = new ArrayList<>(
            List.of(
                    new Offset2D(7, 0),
                    new Offset2D(7, 1),
                    new Offset2D(7, 2),
                    new Offset2D(7, 3),
                    new Offset2D(7, 4),
                    new Offset2D(7, 5),
                    new Offset2D(7, 6),
                    new Offset2D(7, 7)
            )
    );

    public static final ArrayList<Offset2D> SILVER_WINNING_CONDITION = new ArrayList<>(
            List.of(
                    new Offset2D(0, 0),
                    new Offset2D(0, 1),
                    new Offset2D(0, 2),
                    new Offset2D(0, 3),
                    new Offset2D(0, 4),
                    new Offset2D(0, 5),
                    new Offset2D(0, 6),
                    new Offset2D(0, 7)
            )
    );

    public static ArrayList<Offset2D> positionsAround(Offset2D offset2D) {
        ArrayList<Offset2D> positionsAround = new ArrayList<>();

        positionsAround.add(new Offset2D(offset2D.row + 1, offset2D.column));
        positionsAround.add(new Offset2D(offset2D.row - 1, offset2D.column));
        positionsAround.add(new Offset2D(offset2D.row , offset2D.column + 1));
        positionsAround.add(new Offset2D(offset2D.row , offset2D.column - 1));

        return positionsAround;
    }
}
