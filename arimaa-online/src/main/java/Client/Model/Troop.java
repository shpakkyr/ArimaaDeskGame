package Client.Model;

import java.util.Objects;

/**
 * Represents a troop in the game.
 * A Troop is defined by its type.
 */
public record Troop(TroopType type) {

    /**
     * Creates a troop from a notation string and associates it with a specific player.
     *
     * @param pieceTypeString The notation string representing the troop type.
     * @return A new Troop object corresponding to the notation string, or null if the string is empty.
     */
    public static Troop createPieceFromNotationPlayerWithSpecificPlayer(String pieceTypeString) {
        if (Objects.equals(pieceTypeString, "")) {
            return null;
        }
        TroopType pieceType = TroopType.fromNotation(pieceTypeString.toLowerCase().charAt(0));
        return new Troop(pieceType);
    }

}
