package Client.Controller;

import Client.Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Saver {
    private static final Logger logger = Logger.getLogger(Saver.class.getName());

    public static void saveGame(ArrayList<GameState> gameState, File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(gameState);
            logger.info("Game saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Failed to save the game.");
        }
    }
}
