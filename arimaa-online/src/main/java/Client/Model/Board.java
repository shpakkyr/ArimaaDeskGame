package Client.Model;

import jdk.jfr.BooleanFlag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import static Client.Model.TroopType.RABBIT;

public class Board {
    public static final int DIMENSION = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
    private final Troop[][] board;
    private ArrayList<Troop> troopList;

    public Board() {
        board = new Troop[DIMENSION][DIMENSION];
    }

    /*public void drawBoard(Graphics2D graphics2D) {
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
    }*/

    public void placeTroop(Troop troop, Offset2D offset2d){
        board[offset2d.getRow()][offset2d.getColumn()] = troop;
    }

    public void removeTroop(Offset2D offset) {
        board[offset.getRow()][offset.getColumn()] = null;
    }

    public Troop getTroopAt(Offset2D offset2D){
        return board[offset2D.getRow()][offset2D.getColumn()];
    }

    public boolean isPositionEmpty(Offset2D offset2D) {
        return getTroopAt(offset2D) == null;
    }

    public boolean isValidOffset(Offset2D offset2D) {
        return offset2D.getRow() >= 0 && offset2D.getRow() < DIMENSION && offset2D.getColumn() >= 0 && offset2D.getColumn() < DIMENSION;
    }

    public void makeStepMove(StepMove move) {
        Offset2D from = move.getFrom();
        Offset2D to = move.getTo();
        Troop troop = getTroopAt(from);
        removeTroop(from);
        placeTroop(troop, to);
    }

    public void makePullMove(PullMove move) {
        makeStepMove(new StepMove(move.getFrom(), move.getTo()));
        makeStepMove(new StepMove(move.getPulledPieceFrom(), move.getPulledPieceTo()));
    }

    public void makePushMove(PushMove move) {
        makeStepMove(new StepMove(move.getFrom(), move.getTo()));
        makeStepMove(new StepMove(move.getPulledPieceFrom(), move.getPulledPieceTo()));
    }

    public Troop[][] emptyBoard() {
        return new Troop[DIMENSION][DIMENSION];
    }

    public boolean isWinner(Player player, Player enemy) {
        /*Check if player's rabbit is on a winning offset*/
        ArrayList<Offset2D> winningOffsets = player.getPlayingSide() == PlayingSide.GOLD ? Offset2D.GOLD_WINNING_CONDITION : Offset2D.SILVER_WINNING_CONDITION;
        for (Offset2D winningOffset : winningOffsets) {
            Troop troop = getTroopAt(winningOffset);
            if (troop != null && troop.getType() == RABBIT && troop.getPlayer() == player)
                return true;
        }

        /*Check if enemy lost all of his rabbits*/
        ArrayList<Troop> enemyTroops = getPlayersTroops(enemy);
        for (int i = 0; i < enemyTroops.size(); i++) {
            Troop troop = enemyTroops.get(i);
            if (troop.getType() == RABBIT) {
                return false;
            }
            if (i == enemyTroops.size() - 1) {
                return true;
            }
        }
        return true;
    }

    public ArrayList<Troop> getPlayersTroops(Player player) {
        ArrayList<Troop> playersTroops = new ArrayList<>();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (board[i][j] != null && board[i][j].getPlayer() == player)
                    playersTroops.add(board[i][j]);
            }
        }
        return playersTroops;
    }

    public void switchTroops(Offset2D offset1, Offset2D offset2) {
        Troop firstTroop = getTroopAt(offset1);
        Troop secondTroop = getTroopAt(offset2);
        placeTroop(firstTroop, offset2);
        placeTroop(secondTroop, offset1);
    }

    public boolean isFriendlyTroopAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = troop.getPlayer();
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRaw();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
            Troop newTroop = getTroopAt(newOffset2d);
            if (newTroop != null && newTroop.getPlayer() == player)
                return true;
        }
        return false;
    }

    public boolean isEnemyTroopAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = troop.getPlayer();
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRaw();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
            Troop newTroop = getTroopAt(newOffset2d);
            if (newTroop != null && newTroop.getPlayer() != player)
                return true;
        }
        return false;
    }

    public boolean isStrongerEnemyNearby(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = troop.getPlayer();
        ArrayList<Offset2D> positionsAround = Offset2D.positionsAround(offset2D);

        for (Offset2D position : positionsAround) {
            Troop troopCheck = getTroopAt(position);
            Player playerCheck = troopCheck.getPlayer();
            if (player != playerCheck && troopCheck.getType().isStrongerThan(troop.getType()))
                return true;
        }
        return false;
    }

    public boolean isTroopFrozen(Offset2D offset2D) {
        return isStrongerEnemyNearby(offset2D) && !isFriendlyTroopAround(offset2D);
    }

    public ArrayList<Troop> getTroopList() {
        return troopList;
    }
}
