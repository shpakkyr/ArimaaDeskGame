package Client.Controller;

import Client.Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Loader class to handle loading game states from a file.
 */
public class Loader {
    private static final Logger logger = Logger.getLogger(Loader.class.getName());

    /**
     * Loads the game state from a specified file.
     *
     * @param file The file from which to load the game state.
     * @return An ArrayList of GameState objects if the load is successful, otherwise null.
     */
    public static ArrayList<GameState> loadGame(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<GameState>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.info("Failed to load the game.");
            return null;
        }
    }
}
