package Client.Controller;

import Client.Model.Board;
import Client.Model.GameModel;
import Client.Model.Player;
import Client.Model.PlayingSide;
import Client.View.GameView;

public class Launcher {
    public static void main(String[] args) {
        GameModel gameModel = new GameModel(new Player(1, PlayingSide.GOLD, "Kyrylo"), new Player(2, PlayingSide.SILVER, "Bogdan"), new Board());
        GameController gameController = new GameController(gameModel);
        GameView gameView = new GameView(gameController);
        gameView.init();
    }
}
