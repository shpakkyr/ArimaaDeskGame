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

    public int getStrength() {
        return strength;
    }
    public String getName() {
        return name;
    }

    public boolean isStrongerThan(TroopType troopType) {
        return this.strength > troopType.strength;
    }

}
