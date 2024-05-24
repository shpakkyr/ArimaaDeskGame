package Client.Controller;

import Client.Model.GameModel;
import Client.Model.Player;
import Client.View.GameView;

import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel game = new GameModel(new Player(1, "", false), new Player(2, "", false));
            GameView view = new GameView(game);
            view.init();
        });

    }
}
