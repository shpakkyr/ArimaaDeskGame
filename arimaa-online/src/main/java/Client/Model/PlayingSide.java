package Client.Model;

public enum PlayingSide {
    SILVER("silver"),
    GOLD("gold");

    private final String playingSide;

    PlayingSide(String playingSide) {
        this.playingSide = playingSide;
    }

    public String getPlayingSide() {
        return playingSide;
    }
}
