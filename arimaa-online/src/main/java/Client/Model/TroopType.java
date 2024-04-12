package Client.Model;

public enum TroopType {
    ELEPHANT(2, "elephant"),
    CAMEL(1, "camel"),
    HORSE(2, "horse"),
    DOG(2, "dog"),
    CAT(2, "cat"),
    RABBIT(8, "rabbit");

    private final int total;
    private final String name;

    TroopType(int total, String name) {
        this.total = total;
        this.name = name;
    }

    public int getTotal() {
        return total;
    }
    public String getName() {
        return name;
    }

}
