package Client.View;

import java.awt.*;

/**
 * Enum representing the different game modes in the application.
 * Each game mode has an associated color and a name.
 */
public enum GameMode {
    NONE(Color.PINK, "None"),
    SWITCH(Color.PINK, "Switch"),
    STEP(Color.PINK, "Step"),
    PULL(Color.PINK, "Pull"),
    PUSH(Color.PINK, "Push");
    private final Color color;
    private final String modeName;

    /**
     * Constructor to initialize the game mode with a specific color and name.
     *
     * @param color    The color associated with the game mode.
     * @param modeName The name of the game mode.
     */
    GameMode(Color color, String modeName){
        this.color = color;
        this.modeName = modeName;

    }

    public Color getColor(){
        return this.color;
    }

    public String getModeName(){
        return this.modeName;
    }
}
