package Client.Model;

/**
 * Represents a push/pull move in the game.
 */
public class ComplexMove extends Move{
    private final Offset2D movedPieceFrom;
    private final Offset2D movedPieceTo;

    /**
     * Constructs a ComplexMove object with the specified positions.
     *
     * @param from The starting position of the main piece.
     * @param to The ending position of the main piece.
     * @param pulledPieceFrom The starting position of the pulled piece.
     * @param pulledPieceTo The ending position of the pulled piece.
     */
    public ComplexMove(Offset2D from, Offset2D to, Offset2D pulledPieceFrom, Offset2D pulledPieceTo) {
        super(from, to);
        this.movedPieceFrom = pulledPieceFrom;
        this.movedPieceTo = pulledPieceTo;
    }

    public Offset2D getMovedPieceFrom() {
        return movedPieceFrom;
    }

    public Offset2D getMovedPieceTo() {
        return movedPieceTo;
    }
}
