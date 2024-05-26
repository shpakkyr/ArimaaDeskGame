package Client.Model;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.*;

import static Client.Model.TroopType.RABBIT;

/**
 * The Board class represents the game board and contains methods for manipulating and querying the board state.
 */
public class Board implements Serializable {
    public static final int DIMENSION = 8;
    private final Tile[][] board;

    /**
     * Constructs an empty board with the specified dimensions.
     */
    public Board() {
        board = new Tile[DIMENSION][DIMENSION];
    }

    /**
     * Places a troop on the board at the specified position.
     *
     * @param troop The troop to place.
     * @param offset2d The position to place the troop.
     */
    public void placeTroop(Troop troop, Offset2D offset2d){
        board[offset2d.getRow()][offset2d.getColumn()] = new Tile(troop);
    }

    /**
     * Places a player on a tile at the specified position.
     *
     * @param player The player to place.
     * @param offset2D The position to place the player.
     */
    public void placePlayerOnTile(Player player, Offset2D offset2D) {
        getTileAt(offset2D).setPlayer(player);
    }

    /**
     * Removes a troop from the specified position.
     *
     * @param offset The position to remove the troop from.
     */
    public void removeTroop(Offset2D offset) {
        board[offset.getRow()][offset.getColumn()].setTroop(null);
    }

    /**
     * Removes a player from the specified position.
     *
     * @param offset The position to remove the player from.
     */
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

    /**
     * Checks if the position is empty (no troop).
     *
     * @param offset2D The position to check.
     * @return true if the position is empty, false otherwise.
     */
    public boolean isPositionEmpty(Offset2D offset2D) {
        return getTroopAt(offset2D) == null;
    }

    /**
     * Executes a step move on the board.
     *
     * @param move The step move to execute.
     */
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

    /**
     * Checks if the player has won the game.
     *
     * @param player The player to check for a win.
     * @param enemy The opposing player.
     * @param playerId id of a player(1 or 2)
     * @return true if the player has won, false otherwise.
     */
    public boolean isWinner(Player player, Player enemy, int playerId) {
        LinkedList<Offset2D> winningOffsets = null;
        if (playerId == 1)
            winningOffsets = player.getPlayingSide() == PlayingSide.GOLD ? Offset2D.GOLD_WINNING_CONDITION : Offset2D.SILVER_WINNING_CONDITION;
        else if (playerId == 2)
            winningOffsets = player.getPlayingSide() == PlayingSide.GOLD ? Offset2D.SILVER_WINNING_CONDITION : Offset2D.GOLD_WINNING_CONDITION;
        if (winningOffsets != null) {
            for (Offset2D winningOffset : winningOffsets) {
                Troop troop = getTroopAt(winningOffset);
                Player playerCheck = getPlayerAt(winningOffset);
                if (troop != null && troop.type() == RABBIT && playerCheck == player)
                    return true;
            }
        }

        List<Troop> enemyTroops = getPlayersTroops(enemy);
        for (int i = 0; i < enemyTroops.size(); i++) {
            Troop troop = enemyTroops.get(i);
            if (troop.type() == RABBIT) {
                return false;
            }
            if (i == enemyTroops.size() - 1) {
                return true;
            }
        }

        int enemyPositionsAmount = getPlayersTroopsPositions(enemy).size();
        int enemyFrozenPositions = 0;
        for (Offset2D offset2D : getPlayersTroopsPositions(enemy)){
            if (isTroopFrozen(offset2D))
                enemyFrozenPositions++;
        }
        return enemyPositionsAmount == enemyFrozenPositions;
    }

    /**
     * Retrieves all troops belonging to the specified player.
     *
     * @param player The player whose troops are to be retrieved.
     * @return A list of troops belonging to the player.
     */
    public List<Troop> getPlayersTroops(Player player) {
        List<Troop> playersTroops = new LinkedList<>();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (board[i][j] != null && board[i][j].getPlayer() == player && board[i][j].getTroop() != null)
                    playersTroops.add(board[i][j].getTroop());
            }
        }
        return playersTroops;
    }

    /**
     * Retrieves the positions of all troops belonging to the specified player.
     *
     * @param player The player whose troop positions are to be retrieved.
     * @return A list of positions of the player's troops.
     */
    public List<Offset2D> getPlayersTroopsPositions(Player player) {
        List<Offset2D> playersTroops = new LinkedList<>();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (board[i][j].getTroop() != null && board[i][j].getPlayer() != null && board[i][j].getPlayer().equals(player))
                    playersTroops.add(new Offset2D(i, j));
            }
        }
        return playersTroops;
    }

    /**
     * Switches the positions of two troops on the board.
     *
     * @param offset1 The position of the first troop.
     * @param offset2 The position of the second troop.
     */
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

    /**
     * Checks if there is a friendly troop around the specified position.
     *
     * @param offset2D The position to check.
     * @return True if there is a friendly troop around, otherwise false.
     */
    public boolean isFriendlyTroopAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getTileAt(offset2D).getPlayer();
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRow();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            if (newRaw < 0 || newRaw >= 8 || newColumn < 0 || newColumn >= 8)
                continue;
            Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
            if (getTroopAt(newOffset2d) != null && getPlayerAt(newOffset2d) == player)
                return true;
        }
        return false;
    }

    /**
     * Retrieves the positions of stronger enemy troops around the specified position.
     *
     * @param offset2D The position to check around.
     * @return A list of positions of stronger enemy troops.
     */
    public List<Offset2D> getStrongerEnemiesPositions(Offset2D offset2D) {
        List<Offset2D> enemyPositions = new LinkedList<>();
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        for (Directions direction : Directions.values()) {
            int newRaw = offset2D.getRow() + direction.getRow();
            int newColumn = offset2D.getColumn() + direction.getColumn();
            if (newRaw >= 0 && newRaw < 8 && newColumn >= 0 && newColumn < 8) {
                Offset2D newOffset2d = new Offset2D(newRaw, newColumn);
                if (getTroopAt(newOffset2d) != null
                        && getPlayerAt(newOffset2d) != player
                        && getTroopAt(newOffset2d).type().isStrongerThan(troop.type())
                        && !isTroopFrozen(newOffset2d)
                )
                    enemyPositions.add(newOffset2d);
            }
        }
        return enemyPositions;
    }

    /**
     * Checks if there is a stronger enemy nearby the specified position.
     *
     * @param offset2D The position to check.
     * @return True if there is a stronger enemy nearby, otherwise false.
     */
    public boolean isStrongerEnemyNearby(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        List<Offset2D> positionsAround = Offset2D.positionsAround(offset2D);

        for (Offset2D position : positionsAround) {
            Troop troopCheck = getTroopAt(position);
            Player playerCheck = getPlayerAt(position);
            if(troopCheck != null && playerCheck != null) {
                if (player != playerCheck && troopCheck.type().isStrongerThan(troop.type()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the positions of stronger enemy troops around the specified position.
     *
     * @param offset2D The position to check around.
     * @return A list of positions of stronger enemy troops.
     */
    public List<Offset2D> getStrongerEnemiesPositionsAround(Offset2D offset2D) {
        Troop troop = getTroopAt(offset2D);
        Player player = getPlayerAt(offset2D);
        List<Offset2D> positionsAround = Offset2D.positionsAround(offset2D);
        List<Offset2D> positionsOfEnemy = new LinkedList<>();

        for (Offset2D position : positionsAround) {
            Troop troopCheck = getTroopAt(position);
            Player playerCheck = getPlayerAt(position);
            if(troopCheck != null && playerCheck != null) {
                if (player != playerCheck && troopCheck.type().isStrongerThan(troop.type()))
                    positionsOfEnemy.add(position);
            }
        }
        return positionsOfEnemy;
    }

    /**
     * Checks if the troop at the specified position is frozen.
     *
     * @param offset2D The position to check.
     * @return True if the troop is frozen, otherwise false.
     */
    public boolean isTroopFrozen(Offset2D offset2D) {
        return isStrongerEnemyNearby(offset2D) && !isFriendlyTroopAround(offset2D);
    }

    /**
     * Checks if the specified position is out of bounds.
     *
     * @param offset2D The position to check.
     * @return True if the position is out of bounds, otherwise false.
     */
    public boolean isPositionOutOfBound(Offset2D offset2D) {
        return offset2D.getRow() < 0 || offset2D.getRow() > 7 || offset2D.getColumn() < 0 || offset2D.getColumn() > 7;
    }

    /**
     * Retrieves the positions a troop can step on from the specified position.
     *
     * @param offset2D The starting position of the troop.
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return A list of positions the troop can step on.
     */
    public List<Offset2D> positionsTroopCanStepOn(Offset2D offset2D, boolean isSilverBottom) {
        List<Directions> directions = getTileAt(offset2D).troopValidDirections(isSilverBottom);
        List<Offset2D> positionsToStep = new LinkedList<>();
        if (isTroopFrozen(offset2D))
            return positionsToStep;

        for (Directions direction : directions) {
            int newRow = offset2D.getRow()+direction.getRow();
            int newCol = offset2D.getColumn()+direction.getColumn();
            Offset2D newPos = new Offset2D(newRow, newCol);
            if (!isPositionOutOfBound(newPos) && getTileAt(newPos).getTroop() == null && getTileAt(newPos).getPlayer() == null)
                    positionsToStep.add(newPos);
        }
        return positionsToStep;
    }

    /**
     * Checks if the specified position can step to another position.
     *
     * @param offset2D The position to check.
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return True if the position can step, otherwise false.
     */
    private boolean canStepPosition(Offset2D offset2D, boolean isSilverBottom) {
        return !positionsTroopCanStepOn(offset2D, isSilverBottom).isEmpty();
    }

    /**
     * Checks if the specified position can pull an enemy.
     *
     * @param offset2D The position to check.
     * @return True if the position can pull, otherwise false.
     */
    private boolean canPullPosition(Offset2D offset2D) {
        return !getStrongerEnemiesPositions(offset2D).isEmpty();
    }

    /**
     * Retrieves the positions of the player's troops that can step.
     *
     * @param player The player whose troop positions are to be retrieved.
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return A list of positions of the player's troops that can step.
     */
    public List<Offset2D> canStepPositions(Player player, boolean isSilverBottom) {
        List<Offset2D> positionsOfTroop = new LinkedList<>();

        for (Offset2D position : getPlayersTroopsPositions(player)) {
            if (canStepPosition(position, isSilverBottom)) {
                positionsOfTroop.add(position);
            }
        }
        return positionsOfTroop;
    }

    /**
     * Retrieves the positions of the enemy's troops that can be pulled.
     *
     * @param enemy The enemy player.
     * @return A list of positions of the enemy's troops that can be pulled.
     */
    public List<Offset2D> canPullPositions(Player enemy) {
        List<Offset2D> positionsOfEnemyTroops = getPlayersTroopsPositions(enemy);
        List<Offset2D> positionsOfEnemyTroopsToBePulled = new LinkedList<>();

        for (Offset2D offset2D : positionsOfEnemyTroops) {
            if (canPullPosition(offset2D))
                positionsOfEnemyTroopsToBePulled.add(offset2D);
        }
        return positionsOfEnemyTroopsToBePulled;
    }

    /**
     * Retrieves valid step moves for a position by itself.
     *
     * @param position The position to check.
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return A list of valid step moves.
     */
    public List<StepMove> getValidStepMovesByItselfForPosition(Offset2D position, boolean isSilverBottom){
        List<StepMove> stepMoveList = new LinkedList<>();
        Tile tile = getTileAt(position);
        List<Directions> directionsList = tile.troopValidDirections(isSilverBottom);
        if (isTroopFrozen(position)) return new LinkedList<>();
        for (Offset2D onePosition : position.getAdjacentPositions(directionsList)){
            if(isPositionEmpty(onePosition)){
                stepMoveList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveList;
    }

    /**
     * Retrieves the positions of stronger adjacent enemy pieces.
     *
     * @param position The position to check.
     * @return A list of positions of stronger adjacent enemy pieces.
     */
    public LinkedList<Offset2D> getPositionsOfStrongerAdjacentEnemyPieces(Offset2D position){
        LinkedList<Offset2D> positionsList = new LinkedList<>();
        Troop piece = getTroopAt(position);
        Player owner = getPlayerAt(position);
        for (Offset2D onePosition : position.getAdjacentPositions(Directions.get4Directions())){
            Troop adjacentPiece = getTroopAt(onePosition);
            if (adjacentPiece != null && getPlayerAt(onePosition) != owner && getTroopAt(onePosition).type().isStrongerThan(piece.type())){
                positionsList.add(onePosition);
            }
        }
        return positionsList;
    }

    /**
     * Retrieves valid pull moves for the puller and pulled positions.
     *
     * @param pullingPiecePosition The position of the pulling piece.
     * @param pulledPiecePosition The position of the pulled piece.
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return A list of valid pull moves.
     */
    public List<ComplexMove> getValidPullMovesForPullerAndPulled(Offset2D pullingPiecePosition, Offset2D pulledPiecePosition, boolean isSilverBottom){
        List<ComplexMove> pullMoveList = new LinkedList<>();
        for (StepMove pullerStepMove : getValidStepMovesByItselfForPosition(pullingPiecePosition, isSilverBottom)){
            pullMoveList.add(new ComplexMove(
                    pullerStepMove.getFrom(),
                    pullerStepMove.getTo(),
                    pulledPiecePosition,
                    pullingPiecePosition
            ));
        }
        return pullMoveList;
    }

    /**
     * Retrieves the positions of possible pushing pieces for the specified position.
     *
     * @param pushedPiecePosition The position to check.
     * @return A list of positions of possible pushing pieces.
     */
    public LinkedList<Offset2D> getPositionsOfPossiblePushingPieces(Offset2D pushedPiecePosition){
        return getPositionsOfStrongerAdjacentEnemyPieces(pushedPiecePosition);
    }

    /**
     * Retrieves the positions of possible pulling pieces for the specified position.
     *
     * @param pulledPiecePosition The position to check.
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return A list of positions of possible pulling pieces.
     */
    public List<Offset2D> getPositionsOfPossiblePullingPieces(Offset2D pulledPiecePosition, boolean isSilverBottom){
        List<Offset2D> pullingPieces = new LinkedList<>();
        for (Offset2D pullingPiecePosition : getStrongerEnemiesPositionsAround(pulledPiecePosition)){
            if (canStepPosition(pullingPiecePosition, isSilverBottom))
                pullingPieces.add(pullingPiecePosition);
        }
        return pullingPieces;
    }

    /**
     * Retrieves valid step moves for pushing and pulling from the specified position.
     *
     * @param position The position to check.
     * @return A list of valid step moves.
     */
    public List<StepMove> getValidStepMovesByPushingPullingForPosition(Offset2D position){
        List<StepMove> stepMoveList = new LinkedList<>();
        List<Directions> directionsList = Directions.get4Directions();
        for (Offset2D onePosition : position.getAdjacentPositions(directionsList)){
            if(isPositionEmpty(onePosition)){
                stepMoveList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveList;

    }

    /**
     * Retrieves valid push moves for the pusher and pushed positions.
     *
     * @param pushingPiecePosition The position of the pushing piece.
     * @param pushedPiecePosition The position of the pushed piece.
     * @return A list of valid push moves.
     */
    public List<ComplexMove> getValidPushMovesForPusherAndPushed(Offset2D pushingPiecePosition, Offset2D pushedPiecePosition){
        List<ComplexMove> pushMoveList = new LinkedList<>();
        for (StepMove pushedStepMove : getValidStepMovesByPushingPullingForPosition(pushedPiecePosition)){
            pushMoveList.add(new ComplexMove(
                    pushingPiecePosition,
                    pushedPiecePosition,
                    pushedStepMove.getFrom(),
                    pushedStepMove.getTo()
            ));
        }
        return pushMoveList;
    }

    /**
     * Retrieves the positions of a player's troops based on the game mode.
     *
     * @param player The player whose troop positions are to be retrieved.
     * @param enemy The enemy player.
     * @param gameMode The game mode ("SWITCH", "STEP", "PULL", "PUSH").
     * @param isSilverBottom parameter to know if user playing as silver player
     * @return A list of positions of the player's troops.
     */
    public List<Offset2D> getPositionsOfPlayersTroops(Player player, Player enemy, String gameMode, boolean isSilverBottom) {
        if (gameMode.equals("SWITCH")) {
            return getPlayersTroopsPositions(player);
        } else if (gameMode.equals("STEP")) {
            return canStepPositions(player, isSilverBottom);
        } else if (gameMode.equals("PULL")) {
            return canPullPositions(enemy);
        } else if (gameMode.equals("PUSH")) {
            return canPullPositions(enemy);
        }
        return null;
    }

    /**
     * Populates the board from a 2D string array representation.
     *
     * @param board2DString The 2D string array representation of the board.
     * @param player1 The first player.
     * @param player2 The second player.
     */
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
                Troop piece = Troop.createPieceFromNotationPlayerWithSpecificPlayer(stringPiece);
                placeTroop(piece, position);
                placePlayerOnTile(player, position);
            }
        }
    }

    /**
     * Populates the board from a 2D string array representation but rotated on 180 degrees.
     *
     * @param board2DString The 2D string array representation of the board.
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public void populateBoardFrom2DStringRotated(String[][] board2DString, Player player1, Player player2){
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                Offset2D position = new Offset2D(7 - i, 7 - j);
                String stringPiece = board2DString[i][j];
                if (Objects.equals(stringPiece, "")){
                    placeTroop(null, position);
                    continue;
                };
                Player player = null;
                if (Character.isUpperCase(stringPiece.charAt(0))) {
                    player = player1.getPlayingSide() == PlayingSide.GOLD ? player1 : player2;
                } else {
                    player = player1.getPlayingSide() == PlayingSide.SILVER ? player1 : player2;
                }
                Troop piece = Troop.createPieceFromNotationPlayerWithSpecificPlayer(stringPiece);
                placeTroop(piece, position);
                placePlayerOnTile(player, position);
            }
        }
    }

    /**
     * Creates a copy of the board.
     *
     * @return A 2D string array representing the copy of the board.
     */
    public String[][] copyBoard() {
        String[][] copy = new String[8][8];
        for(int j = 0; j < DIMENSION; j++) {
            for (int i = 0; i < DIMENSION; i++) {
                if(this.board[j][i].getTroop() == null){
                    copy[j][i] = "";
                }else {
                    String playerCheck = TroopType.toNotation(this.board[j][i].getTroop().type());
                    if(this.board[j][i].getPlayer().getPlayerId() == 1){
                        copy[j][i] = playerCheck.toUpperCase();
                    }else{
                        copy[j][i] = playerCheck;
                    }
                }
            }
        }
        return copy;
    }
}
