package Client.Model;

public class ComplexMove extends Move{
    private final Offset2D movedPieceFrom;
    private final Offset2D movedPieceTo;
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
