package Client.Model;

import java.util.ArrayList;
import java.util.List;

public enum Directions {
    NORTH(1, 0),
    SOUTH(-1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int raw;
    private final int column;

    Directions(int raw, int column) {
        this.raw = raw;
        this.column = column;
    }

    public int getRaw() {
        return raw;
    }

    public int getColumn() {
        return column;
    }

    public ArrayList<Directions> get4Directions() {
        return new ArrayList<Directions>(List.of(NORTH, SOUTH, LEFT, RIGHT));
    }
}
