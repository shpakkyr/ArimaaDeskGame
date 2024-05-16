package Client.View;

import java.awt.*;

public enum GameMode {
    NONE(Color.PINK, "None"),
    SWITCH(Color.PINK, "Switch"),
    STEP(Color.PINK, "Step"),
    PULL(Color.PINK, "Pull"),
    PUSH(Color.PINK, "Push");
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
