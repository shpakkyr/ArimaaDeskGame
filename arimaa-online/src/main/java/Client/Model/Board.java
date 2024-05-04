package Client.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Board {
    public static final int DIMENSION = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
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

    public void drawBoard(Graphics2D graphics2D) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/images/img.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
        for (int row = 0; row < DIMENSION; row++) {
            for (int col = 0; col < DIMENSION; col++) {
                graphics2D.drawImage(image, col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
            }
        }
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
