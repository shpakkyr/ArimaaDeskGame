package Client.Controller;

import Client.Model.GameModel;
import Client.Model.Player;
import Client.View.GameView;

import javax.swing.*;

/**
 * Launcher class to start the game application.
 * It initializes the game model with two players and sets up the game view.
 */
public class Launcher {

    /**
     * The main method to launch the game application.
     * It uses SwingUtilities.invokeLater to ensure that the GUI creation and updates are done on the Event Dispatch Thread.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize the game model with two players
            GameModel game = new GameModel(new Player(1, "", false), new Player(2, "", false));
            GameView view = new GameView(game);
            view.init();
        });

    }
}
