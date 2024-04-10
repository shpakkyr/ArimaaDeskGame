package Client.Model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BoardPos implements TilePos {
    private final int dimension;
    private final int i;
    private final int j;

    public BoardPos(int dimension, int i, int j) {
        this.dimension = dimension;
        this.i = i;
        this.j = j;
    }

    @Override
    public int i() {
        return i;
    }

    @Override
    public int j() {
        return j;
    }

    @Override
    public char column() {
        return (char) ('a' + i);
    }

    @Override
    public int row() {
        return j + 1;
    }

    public TilePos step(int columnStep, int rowStep) {
        int newi = i + columnStep;
        int newj = j + rowStep;

        if ((newi >= 0 && newi < dimension) &&
                (newj >= 0 && newj < dimension)) {
            return new BoardPos(dimension, newi, newj);
        }

        return TilePos.OFF_BOARD;
    }

    @Override
    public TilePos step(Offset2D step) {
        return step(step.x, step.y);
    }

    @Override
    public List<BoardPos> neighbours() {
        //TODO
        return new ArrayList<>();
    }

    @Override
    public boolean isNextTo(TilePos pos) {
        //TODO
        return true;
    }

    @Override
    public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
        return side == PlayingSide.SILVER ?
                step(dir) :
                step(dir.yFlipped());
    }

    @Override
    public boolean equalsTo(int i, int j) {
        return this.i == i && this.j == j;
    }

    @Override
    public boolean equals(Object obj) {
        //TODO
        return true;
    }
}
