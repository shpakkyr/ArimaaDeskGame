package Client.Controller;

import Client.Model.GameModel;
import Client.Model.Player;
import Client.View.GameView;

import javax.swing.*;

public class Launcher {
    private static JFrame frame;
    private static JPanel currentRightPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Arimaa");
            GameModel game = new GameModel(new Player(1, "", false), new Player(2, "", false));
            GameView view = new GameView(game);
            view.init();
        });

    }
}
