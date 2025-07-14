package Client.Model;

/**
 * Represents a simple step move in the game.
 * A step move involves moving a piece from one position to another.
 */
public class StepMove extends Move{

    /**
     * Constructs a StepMove object with the specified starting and ending positions.
     *
     * @param from The starting position of the move.
     * @param to The ending position of the move.
     */
    public StepMove(Offset2D from, Offset2D to) {
        super(from, to);
    }
}
