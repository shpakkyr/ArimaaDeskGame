package Client.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing the four main directions: North, South, Left, and Right.
 */
public enum Directions {
    NORTH(-1, 0),
    SOUTH(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int row;
    private final int column;

    /**
     * Constructs a Directions enum with the specified row and column offsets.
     *
     * @param row The row offset for the direction.
     * @param column The column offset for the direction.
     */
    Directions(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static ArrayList<Directions> get4Directions() {
        return new ArrayList<Directions>(List.of(NORTH, SOUTH, LEFT, RIGHT));
    }
}
