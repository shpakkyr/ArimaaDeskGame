package Client.Model;

public class PullMove extends Move{
    public PullMove(Offset2D from, Offset2D to, Offset2D pulledPieceFrom, Offset2D pulledPieceTo) {
        super(from, to, pulledPieceFrom, pulledPieceTo, "Pull");
    }
}
