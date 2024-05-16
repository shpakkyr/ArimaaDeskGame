package Client.Model;

public class PullMove extends Move{
    private final Offset2D pulledPieceFrom;
    private final Offset2D pulledPieceTo;
    public PullMove(Offset2D from, Offset2D to, Offset2D pulledPieceFrom, Offset2D pulledPieceTo) {
        super(from, to);
        this.pulledPieceFrom = pulledPieceFrom;
        this.pulledPieceTo = pulledPieceTo;
    }

    public Offset2D getPulledPieceFrom() {
        return pulledPieceFrom;
    }

    public Offset2D getPulledPieceTo() {
        return pulledPieceTo;
    }
}
