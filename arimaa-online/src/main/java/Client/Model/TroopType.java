package Client.Model;

public enum TroopType {
    ELEPHANT(6, "elephant"),
    CAMEL(5, "camel"),
    HORSE(4, "horse"),
    DOG(3, "dog"),
    CAT(2, "cat"),
    RABBIT(1, "rabbit");

    private final int strength;
    private final String name;

    TroopType(int strength, String name) {
        this.strength = strength;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isStrongerThan(TroopType troopType) {
        return this.strength > troopType.strength;
    }

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
