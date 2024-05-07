package Client.Model;

public class PushMove extends Move{
    public PushMove(Offset2D from, Offset2D to, Offset2D pushedPieceFrom, Offset2D pushedPieceTo) {
        super(from, to, pushedPieceFrom, pushedPieceTo, "Push");
    }
}
