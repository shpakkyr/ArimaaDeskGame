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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Offset2D offset2D = (Offset2D) obj;
        return row == offset2D.row && column == offset2D.column;
    }

    public Offset2D getAdjacentPosition(Directions direction) {
        int newRow = this.row + direction.getRow();
        int newColumn = this.column + direction.getColumn();

        if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
            return new Offset2D(newRow, newColumn);
        }

        return null;
    }

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


}
