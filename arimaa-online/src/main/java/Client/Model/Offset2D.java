package Client.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a 2D offset or position on the board.
 */
public class Offset2D {
    private int row;
    private int column;

    /**
     * Constructs an Offset2D object with the specified row and column.
     *
     * @param row The row of the offset.
     * @param column The column of the offset.
     */
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

    /**
     * List of predefined trap offsets on the board.
     */
    public static final ArrayList<Offset2D> TRAP_OFFSET = new ArrayList<>(
            List.of(
                    new Offset2D(2, 2),
                    new Offset2D(2, 5),
                    new Offset2D(5, 2),
                    new Offset2D(5, 5)
            )
    );

    /**
     * List of predefined silver winning condition offsets on the board.
     */
    public static final ArrayList<Offset2D> SILVER_WINNING_CONDITION = new ArrayList<>(
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

    /**
     * List of predefined gold winning condition offsets on the board.
     */
    public static final ArrayList<Offset2D> GOLD_WINNING_CONDITION = new ArrayList<>(
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

    /**
     * Gets the positions around the specified offset.
     *
     * @param offset2D The offset to get positions around.
     * @return A list of positions around the specified offset.
     */
    public static ArrayList<Offset2D> positionsAround(Offset2D offset2D) {
        ArrayList<Offset2D> positionsAround = new ArrayList<>();

        for (int i = -1; i < 2; i += 2) {
            int newRow = offset2D.row + i;
            int newCol = offset2D.column + i;

            if (newRow >= 0 && newRow < 8)
                positionsAround.add(new Offset2D(newRow, offset2D.column));
            if (newCol >= 0 && newCol < 8)
                positionsAround.add(new Offset2D(offset2D.row, newCol));
        }

        return positionsAround;
    }

    /**
     * Gets the adjacent position in the specified direction.
     *
     * @param direction The direction to get the adjacent position.
     * @return The adjacent position, or null if out of bounds.
     */
    public Offset2D getAdjacentPosition(Directions direction) {
        int newRow = this.row + direction.getRow();
        int newColumn = this.column + direction.getColumn();

        if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
            return new Offset2D(newRow, newColumn);
        }

        return null;
    }

    /**
     * Gets the adjacent positions in the specified directions.
     *
     * @param directionArrayList The list of directions to get adjacent positions.
     * @return A list of adjacent positions.
     */
    public ArrayList<Offset2D> getAdjacentPositions(ArrayList<Directions> directionArrayList) {
        ArrayList<Offset2D> positionsArrayList = new ArrayList<>();
        for (Directions direction : directionArrayList) {
            Offset2D adjacentPosition = getAdjacentPosition(direction);
            if (adjacentPosition != null) {
                positionsArrayList.add(adjacentPosition);
            }
        }
        return positionsArrayList;
    }

    /**
     * Compares this offset to the specified object. The result is true if and only if the argument is not null
     * and is an Offset2D object that represents the same row and column as this object.
     *
     * @param obj The object to compare this Offset2D against.
     * @return true if the given object represents an Offset2D equivalent to this offset, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Offset2D offset2D = (Offset2D) obj;
        return row == offset2D.row && column == offset2D.column;
    }

    /**
     * Returns a hash code value for this offset.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
