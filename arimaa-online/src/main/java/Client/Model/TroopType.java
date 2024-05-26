package Client.Model;

/**
 * Enum representing different types of troops in the game, each with a specific strength and name.
 */
public enum TroopType {
    ELEPHANT(6, "elephant"),
    CAMEL(5, "camel"),
    HORSE(4, "horse"),
    DOG(3, "dog"),
    CAT(2, "cat"),
    RABBIT(1, "rabbit");

    private final int strength;
    private final String name;

    /**
     * Constructs a TroopType with the specified strength and name.
     *
     * @param strength The strength of the troop type.
     * @param name     The name of the troop type.
     */
    TroopType(int strength, String name) {
        this.strength = strength;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Determines if this troop type is stronger than the specified troop type.
     *
     * @param troopType The troop type to compare against.
     * @return True if this troop type is stronger, otherwise false.
     */
    public boolean isStrongerThan(TroopType troopType) {
        return this.strength > troopType.strength;
    }

    /**
     * Converts a notation character to the corresponding TroopType.
     *
     * @param notation The notation character representing the troop type.
     * @return The corresponding TroopType, or null if the notation is invalid.
     */
    public static TroopType fromNotation(char notation) {
        return switch (notation) {
            case 'e' -> ELEPHANT;
            case 'm' -> CAMEL;
            case 'h' -> HORSE;
            case 'd' -> DOG;
            case 'c' -> CAT;
            case 'r' -> RABBIT;
            default -> null;
        };
    }

    /**
     * Converts a TroopType to its corresponding notation string.
     *
     * @param troop The TroopType to convert.
     * @return The notation string representing the troop type.
     */
    public static String toNotation(TroopType troop) {
        return switch (troop) {
            case ELEPHANT -> "e";
            case CAMEL -> "m";
            case HORSE -> "h";
            case DOG -> "d";
            case CAT -> "c";
            case RABBIT -> "r";
        };
    }

}
