package Client.Controller;

import Client.Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Saver class to handle saving game states to a file.
 */
public class Saver {
    private static final Logger logger = Logger.getLogger(Saver.class.getName());

    /**
     * Saves the game state to a specified file.
     *
     * @param gameState The game state to save.
     * @param file The file to which the game state will be saved.
     */
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
