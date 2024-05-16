package Client.Model;

import java.util.Objects;

public abstract class Move implements MoveDirections{
    private final Offset2D from;
    private final Offset2D to;

    public Move(Offset2D from, Offset2D to) {
        this.from = from;
        this.to = to;
    }


    public Offset2D getFrom() {
        return from;
    }

    public Offset2D getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public Directions getDirection() {
        int rowDiff = to.getRow() - from.getRow();
        int columnDiff = to.getColumn() - from.getColumn();

        if (rowDiff == 1 && columnDiff == 0) {
            return Directions.SOUTH;
        } else if (rowDiff == -1 && columnDiff == 0) {
            return Directions.NORTH;
        } else if (rowDiff == 0 && columnDiff == 1) {
            return Directions.RIGHT;
        } else if (rowDiff == 0 && columnDiff == -1) {
            return Directions.LEFT;
        } else {
            return null;
        }
    }
}
