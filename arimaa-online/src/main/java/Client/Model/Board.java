package Client.Model;

import java.util.ArrayList;

public class Board {
    private final int DIMENSION = 8;
    private final BoardTile[][] board;
    private ArrayList<Troop> troopList;

    public Board() {
        board = new BoardTile[DIMENSION][DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                board[i][j] = BoardTile.EMPTY;
            }
        }
    }

    private void updateBoard(){
        //TODO
    }

    private Board putTroop(Troop troop){
        //TODO
        return new Board();
    }

    private void changeTurn(){
        //TODO
    }

    private void getCurrentTurn(){
        //TODO
    }

    private Troop getTroopAt(int row, int col){
        //TODO
        return troopList.get(0);
    }

    public ArrayList<Troop> getTroopList() {
        return troopList;
    }
}
