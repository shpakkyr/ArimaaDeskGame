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
import java.util.Objects;

import static Client.Model.TroopType.RABBIT;

public class Board {
    public static final int DIMENSION = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
    private final Tile[][] board;
    private ArrayList<Troop> troopList;

    public Board() {
        board = new Tile[DIMENSION][DIMENSION];
    }

    public void placeTroop(Troop troop, Offset2D offset2d){
        board[offset2d.getRow()][offset2d.getColumn()] = new Tile(troop);
    }

    public void placePlayerOnTile(Player player, Offset2D offset2D) {
        getTileAt(offset2D).setPlayer(player);
    }

    public void removeTroop(Offset2D offset) {
        board[offset.getRow()][offset.getColumn()].setTroop(null);
    }

    public void removePlayer(Offset2D offset) {
        board[offset.getRow()][offset.getColumn()].setPlayer(null);
    }

    public Tile getTileAt(Offset2D offset2D) {
        return board[offset2D.getRow()][offset2D.getColumn()];
    }

    public Troop getTroopAt(Offset2D offset2D){
        return getTileAt(offset2D).getTroop();
    }

    public Player getPlayerAt(Offset2D offset2D) {
        return getTileAt(offset2D).getPlayer();
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
        Player player = getPlayerAt(from);
        removeTroop(from);
        removePlayer(from);
        placeTroop(troop, to);
        placePlayerOnTile(player, to);
    }

    public void makePullMove(PullMove move) {
        makeStepMove(new StepMove(move.getFrom(), move.getTo()));
        makeStepMove(new StepMove(move.getPulledPieceFrom(), move.getPulledPieceTo()));
    }

    public void makePushMove(PushMove move) {
        makeStepMove(new StepMove(move.getFrom(), move.getTo()));
        makeStepMove(new StepMove(move.getPushedPieceFrom(), move.getPushedPieceTo()));
    }

    public Troop[][] emptyBoard() {
        return new Troop[DIMENSION][DIMENSION];
    }

    public boolean isWinner(Player player, Player enemy) {
        ArrayList<Offset2D> winningOffsets = player.getPlayingSide() == PlayingSide.GOLD ? Offset2D.GOLD_WINNING_CONDITION : Offset2D.SILVER_WINNING_CONDITION;
        for (Offset2D winningOffset : winningOffsets) {
            Troop troop = getTroopAt(winningOffset);
            Player playerCheck = getPlayerAt(winningOffset);
            if (troop != null && troop.getType() == RABBIT && playerCheck == player)
                return true;
        }

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
                if (board[i][j] != null && board[i][j].getPlayer() == player && board[i][j].getTroop() != null)
                    playersTroops.add(board[i][j].getTroop());
            }
        }
        return playersTroops;
    }

    public ArrayList<Offset2D> getPlayersTroopsPositions(Player player) {
        ArrayList<Offset2D> playersTroops = new ArrayList<>();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (board[i][j].getTroop() != null && board[i][j].getPlayer() != null && board[i][j].getPlayer().equals(player))
                    playersTroops.add(new Offset2D(i, j));
            }
        }
        return playersTroops;
    }

    public void switchTroops(Offset2D offset1, Offset2D offset2) {
        Troop firstTroop = getTroopAt(offset1);
        Troop secondTroop = getTroopAt(offset2);
        Player player1 = getPlayerAt(offset1);
        Player player2 = getPlayerAt(offset1);
        placeTroop(firstTroop, offset2);
        placePlayerOnTile(player1, offset2);
        placeTroop(secondTroop, offset1);
        placePlayerOnTile(player2, offset1);
    }

    public boolean isFriendlyTroopAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getTileAt(offset2D).getPlayer();
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRow();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
            if (getTroopAt(newOffset2d) != null && getPlayerAt(newOffset2d) == player)
                return true;
        }
        return false;
    }

    public ArrayList<Offset2D> getStrongerEnemiesPositions(Offset2D offset2D, Player playerThatPulls) {
        ArrayList<Offset2D> enemyPositions = new ArrayList<>();
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRow();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            if (newRaw >= 0 && newRaw < 8 && newColumn >= 0 && newColumn < 8) {
                Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
                if (getTroopAt(newOffset2d) != null
                        && getPlayerAt(newOffset2d) != player
                        && getTroopAt(newOffset2d).getType().isStrongerThan(troop.getType())
                        && !isTroopFrozen(newOffset2d)
                )
                    enemyPositions.add(newOffset2d);
            }
        }
        return enemyPositions;
    }

    public boolean isEnemyTroopAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRow();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
            if (getTroopAt(newOffset2d) != null && getPlayerAt(newOffset2d) != player)
                return true;
        }
        return false;
    }

    public boolean isStrongerEnemyNearby(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        ArrayList<Offset2D> positionsAround = Offset2D.positionsAround(offset2D);

        for (Offset2D position : positionsAround) {
            Troop troopCheck = getTroopAt(position);
            Player playerCheck = getPlayerAt(position);
            if(troopCheck != null && playerCheck != null) {
                if (player != playerCheck && troopCheck.getType().isStrongerThan(troop.getType()))
                    return true;
            }
        }
        return false;
    }

    public ArrayList<Offset2D> getStrongerEnemiesPositionsAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        ArrayList<Offset2D> positionsAround = Offset2D.positionsAround(offset2D);
        ArrayList<Offset2D> positionsOfEnemy = new ArrayList<>();

        for (Offset2D position : positionsAround) {
            Troop troopCheck = getTroopAt(position);
            Player playerCheck = getPlayerAt(position);
            if(troopCheck != null && playerCheck != null) {
                if (player != playerCheck && troopCheck.getType().isStrongerThan(troop.getType()))
                    positionsOfEnemy.add(position);
            }
        }
        return positionsOfEnemy;
    }

    public boolean isTroopFrozen(Offset2D offset2D) {
        return isStrongerEnemyNearby(offset2D) && !isFriendlyTroopAround(offset2D);
    }

    public boolean isPositionOutOfBound(Offset2D offset2D) {
        return offset2D.getRow() < 0 || offset2D.getRow() > 7 || offset2D.getColumn() < 0 || offset2D.getColumn() > 7;
    }

    public ArrayList<Offset2D> positionsTroopCanStepOn(Offset2D offset2D) {
        ArrayList<Directions> directions = getTileAt(offset2D).troopValidDirections();
        ArrayList<Offset2D> positionsToStep = new ArrayList<>();
        if (isTroopFrozen(offset2D))
            return positionsToStep;

        for (Directions direction : directions) {
            int newRow = offset2D.getRow()+direction.getRow();
            int newCol = offset2D.getColumn()+direction.getColumn();
            Offset2D newPos = new Offset2D(newRow, newCol);
            if (!isPositionOutOfBound(newPos)) {
                Troop troop = getTroopAt(newPos);
                Player player = getPlayerAt(newPos);
            }
            if (!isPositionOutOfBound(newPos) && getTileAt(newPos).getTroop() == null && getTileAt(newPos).getPlayer() == null)
                    positionsToStep.add(newPos);
        }
        return positionsToStep;
    }

    private boolean canStepPosition(Offset2D offset2D) {
        return !positionsTroopCanStepOn(offset2D).isEmpty();
    }

    private boolean canPullPosition(Offset2D offset2D, Player player) {
        return !getStrongerEnemiesPositions(offset2D, player).isEmpty();
    }

    public ArrayList<Offset2D> canStepPositions(Player player) {
        ArrayList<Offset2D> positionsOfTroop = new ArrayList<>();

        for (Offset2D position : getPlayersTroopsPositions(player)) {
            if (canStepPosition(position)) {
                positionsOfTroop.add(position);
            }
        }
        return positionsOfTroop;
    }

    public ArrayList<Offset2D> canPullPositions(Player player, Player enemy) {
        ArrayList<Offset2D> positionsOfEnemyTroops = getPlayersTroopsPositions(enemy);
        ArrayList<Offset2D> positionsOfEnemyTroopsToBePulled = new ArrayList<>();

        for (Offset2D offset2D : positionsOfEnemyTroops) {
            if (canPullPosition(offset2D, player))
                positionsOfEnemyTroopsToBePulled.add(offset2D);
        }
        return positionsOfEnemyTroopsToBePulled;
    }

    public ArrayList<StepMove> getValidStepMovesByItselfForPosition(Offset2D position){
        ArrayList<StepMove> stepMoveArrayList = new ArrayList<>();
        Tile tile = getTileAt(position);
        ArrayList<Directions> directionsArrayList = tile.troopValidDirections();
        if (isTroopFrozen(position)) return new ArrayList<>();
        for (Offset2D onePosition : position.getAdjacentPositions(directionsArrayList)){
            if(isPositionEmpty(onePosition)){
                stepMoveArrayList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveArrayList;
    }

    public ArrayList<Offset2D> getPositionsOfStrongerAdjacentEnemyPieces(Offset2D position){
        ArrayList<Offset2D> positionsArrayList = new ArrayList<>();
        Troop piece = getTroopAt(position);
        Player owner = getPlayerAt(position);
        for (Offset2D onePosition : position.getAdjacentPositions(Directions.get4Directions())){
            Troop adjacentPiece = getTroopAt(onePosition);
            if (adjacentPiece != null && getPlayerAt(position) != owner && getTroopAt(position).getType().isStrongerThan(piece.getType())){
                positionsArrayList.add(onePosition);
            }
        }
        return positionsArrayList;
    }

    public ArrayList<PullMove> getValidPullMovesForPullerAndPulled(Offset2D pullingPiecePosition, Offset2D pulledPiecePosition){
        ArrayList<PullMove> pullMoveArrayList = new ArrayList<>();
        for (StepMove pullerStepMove : getValidStepMovesByItselfForPosition(pullingPiecePosition)){
            pullMoveArrayList.add(new PullMove(
                    pullerStepMove.getFrom(),
                    pullerStepMove.getTo(),
                    pulledPiecePosition,
                    pullingPiecePosition
            ));
        }
        return pullMoveArrayList;
    }

    public ArrayList<Offset2D> getPositionsOfPossiblePushingPieces(Offset2D pushedPiecePosition){
        return getPositionsOfStrongerAdjacentEnemyPieces(pushedPiecePosition);
    }

    public ArrayList<Offset2D> getPositionsOfPossiblePullingPieces(Offset2D pulledPiecePosition){
        ArrayList<Offset2D> pullingPieces = new ArrayList<>();
        for (Offset2D pullingPiecePosition : getStrongerEnemiesPositionsAround(pulledPiecePosition)){
            if (canStepPosition(pullingPiecePosition))
                pullingPieces.add(pullingPiecePosition);
        }
        return pullingPieces;
    }

    public ArrayList<StepMove> getValidStepMovesByPushingPullingForPosition(Offset2D position){
        ArrayList<StepMove> stepMoveArrayList = new ArrayList<>();
        ArrayList<Directions> directionsArrayList = Directions.get4Directions();
        for (Offset2D onePosition : position.getAdjacentPositions(directionsArrayList)){
            if(isPositionEmpty(onePosition)){
                stepMoveArrayList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveArrayList;

    }

    public ArrayList<PushMove> getValidPushMovesForPusherAndPushed(Offset2D pushingPiecePosition, Offset2D pushedPiecePosition){
        ArrayList<PushMove> pushMoveArrayList = new ArrayList<>();
        for (StepMove pushedStepMove : getValidStepMovesByPushingPullingForPosition(pushedPiecePosition)){
            pushMoveArrayList.add(new PushMove(
                    pushingPiecePosition,
                    pushedPiecePosition,
                    pushedStepMove.getFrom(),
                    pushedStepMove.getTo()
            ));
        }
        return pushMoveArrayList;
    }

    public ArrayList<Offset2D> getPositionsOfPlayersTroops(Player player, Player enemy, String gameMode) {
        if (gameMode.equals("SWITCH")) {
            return getPlayersTroopsPositions(player);
        } else if (gameMode.equals("STEP")) {
            return canStepPositions(player);
        } else if (gameMode.equals("PULL")) {
            return canPullPositions(player, enemy);
        } else if (gameMode.equals("PUSH")) {
            return canPullPositions(player, enemy);
        }
        return null;
    }

    public ArrayList<Troop> getTroopList() {
        return troopList;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void populateBoardFrom2DString(String[][] board2DString, Player player1, Player player2){
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                Offset2D position = new Offset2D(i, j);
                String stringPiece = board2DString[i][j];
                if (Objects.equals(stringPiece, "")){
                    placeTroop(null, position);
                    continue;
                };
                Player player = Character.isUpperCase(stringPiece.charAt(0)) ? player1 : player2;
                Troop piece = Troop.createPieceFromNotationPlayerWithSpecificPlayer(stringPiece, player);
                placeTroop(piece, position);
                placePlayerOnTile(player, position);
            }
        }
    }
}
