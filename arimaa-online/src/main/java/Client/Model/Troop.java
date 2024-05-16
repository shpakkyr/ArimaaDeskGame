package Client.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Troop {
    private final TroopType type;


    public Troop(TroopType type) {
        this.type = type;
    }

    public TroopType getType() {
        return type;
    }

    public static Troop createPieceFromNotationPlayerWithSpecificPlayer(String pieceTypeString, Player player) {
        if (Objects.equals(pieceTypeString, "")) {
            return null;
        }
        TroopType pieceType = TroopType.fromNotation(pieceTypeString.toLowerCase().charAt(0));
        return new Troop(pieceType);
    }

}
