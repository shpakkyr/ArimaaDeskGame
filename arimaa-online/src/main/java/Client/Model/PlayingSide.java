package Client.Model;

/**
 * Enum representing the playing side of a player in the game.
 */
public enum PlayingSide {
    SILVER("silver"),
    GOLD("gold");

    private final String playingSide;

    /**
     * Constructs a PlayingSide enum with the specified side name.
     *
     * @param playingSide The name of the playing side.
     */
    PlayingSide(String playingSide) {
        this.playingSide = playingSide;
    }

    public String getPlayingSide() {
        return playingSide;
    }

}
