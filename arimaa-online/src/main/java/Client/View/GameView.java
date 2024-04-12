package Client.View;

import Client.Controller.GameController;
import Client.Model.Board;
import Client.Model.GameModel;

public class GameView {
    private GameModel model;
    private GameController controller;
    private final Board board;

    public GameView(GameModel model, GameController controller, Board board) {
        this.model = model;
        this.controller = controller;
        this.board = board;
    }

    private void drawBoard(){
        //TODO
    }
}
