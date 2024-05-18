package Client.Controller;

import Client.Model.*;

import java.io.*;
import java.util.logging.Logger;

public class Loader {
    private static final Logger logger = Logger.getLogger(Loader.class.getName());

    public static GameState loadGame(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            GameState gameState = (GameState) ois.readObject();
            return gameState;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.info("Failed to load the game.");
            return null;
        }
    }
}
