package Client.Model;

public class PushMove extends Move{
    private final Offset2D pushedPieceFrom;
    private final Offset2D pushedPieceTo;
    public PushMove(Offset2D from, Offset2D to, Offset2D pushedPieceFrom, Offset2D pushedPieceTo) {
        super(from, to);
        this.pushedPieceFrom = pushedPieceFrom;
        this.pushedPieceTo = pushedPieceTo;
    }

    public Offset2D getPushedPieceFrom() {
        return pushedPieceFrom;
    }

    public Offset2D getPushedPieceTo() {
        return pushedPieceTo;
    }
}
