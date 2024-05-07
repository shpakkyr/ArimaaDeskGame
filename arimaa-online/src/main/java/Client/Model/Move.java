package Client.Model;

import java.util.Objects;

public class Move implements MoveDirections{
    private final Offset2D from;
    private final Offset2D to;
    private final Offset2D pulledPieceFrom;
    private final Offset2D pulledPieceTo;
    private final Offset2D pushedPieceFrom;
    private final Offset2D pushedPieceTo;

    public Move(Offset2D from, Offset2D to) {
        this.from = from;
        this.to = to;
        this.pulledPieceFrom = null;
        this.pulledPieceTo = null;
        this.pushedPieceFrom = null;
        this.pushedPieceTo = null;
    }

    public Move(Offset2D from, Offset2D to, Offset2D specialMoveFrom, Offset2D specialMoveTo, String move) {
        this.from = from;
        this.to = to;
        if(move.equals("Pull"))
        {
            this.pulledPieceFrom = specialMoveFrom;
            this.pulledPieceTo = specialMoveTo;
            this.pushedPieceFrom = null;
            this.pushedPieceTo = null;
        } else if(move.equals("Push")) {
            this.pushedPieceFrom = specialMoveFrom;
            this.pushedPieceTo = specialMoveTo;
            this.pulledPieceFrom = null;
            this.pulledPieceTo = null;
        } else {
            throw new IllegalArgumentException("Invalid move type: " + move);
        }
    }

    public Offset2D getFrom() {
        return from;
    }

    public Offset2D getTo() {
        return to;
    }

    public Offset2D getPulledPieceFrom() {
        return pulledPieceFrom;
    }

    public Offset2D getPulledPieceTo() {
        return pulledPieceTo;
    }

    public Offset2D getPushedPieceFrom() {
        return pushedPieceFrom;
    }

    public Offset2D getPushedPieceTo() {
        return pushedPieceTo;
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

    @Override
    public Directions getPushedDirection() {
        int rowDiff = pushedPieceTo.getRow() - pushedPieceFrom.getRow();
        int columnDiff = pushedPieceTo.getColumn() - pushedPieceFrom.getColumn();

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

    @Override
    public Directions getPulledDirection() {
        int rowDiff = pulledPieceTo.getRow() - pulledPieceFrom.getRow();
        int columnDiff = pulledPieceTo.getColumn() - pulledPieceFrom.getColumn();

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
