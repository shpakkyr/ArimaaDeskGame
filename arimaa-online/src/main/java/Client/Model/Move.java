package Client.Model;

import java.util.Objects;

/**
 * Abstract class representing a move in the game.
 * Implements the MoveDirections interface to provide direction functionality.
 */
public class Move {
    private final Offset2D from;
    private final Offset2D to;

    /**
     * Constructs a Move object with the specified starting and ending positions.
     *
     * @param from The starting position of the move.
     * @param to The ending position of the move.
     */
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

    /**
     * Compares this move to the specified object. The result is true if and only if the argument is not null
     * and is a Move object that represents the same starting and ending positions as this object.
     *
     * @param o The object to compare this Move against.
     * @return true if the given object represents a Move equivalent to this move, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }
}
