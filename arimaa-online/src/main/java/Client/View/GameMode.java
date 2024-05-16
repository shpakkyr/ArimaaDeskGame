package Client.View;

import java.awt.*;

public enum GameMode {
    NONE(Color.LIGHT_GRAY, "None"),
    SWITCH(Color.GREEN, "Switch"),
    STEP(Color.ORANGE, "Step"),
    PULL(Color.MAGENTA, "Pull"),
    PUSH(Color.RED, "Push");
    private final Color color;
    private final String modeName;

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
