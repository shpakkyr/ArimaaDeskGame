package Client.View;

import Client.Model.*;
import Server.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * BoardPanel represents the game board UI and handles user interactions with the game.
 */
public class BoardPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(BoardPanel.class.getName());

    private GameModel game;
    private GameMode currentMode = GameMode.NONE;
    private final JPanel[][] tiles;
    private Client client;

    /**
     * Constructs a BoardPanel with the specified game model.
     *
     * @param game The game model to use for this board.
     */
    public BoardPanel(GameModel game) {
        this.game = game;
        tiles = new JPanel[Board.DIMENSION][Board.DIMENSION];
        setLayout(new GridLayout(Board.DIMENSION, Board.DIMENSION));
        int tileSize = 70;
        MouseAdapter mouseAdapter = createMouseAdapter();
        for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setPreferredSize(new Dimension(tileSize, tileSize));
            int row = i / Board.DIMENSION;
            int col = i % Board.DIMENSION;
            tiles[row][col] = square;
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            square.addMouseListener(mouseAdapter);
            add(square);
        }
        resetSquaresColors();
        int boardWidth = Board.DIMENSION * tileSize;
        int boardHeight = Board.DIMENSION * tileSize;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        fillSquares();
    }

    /**
     * Fills the squares on the board with the appropriate images based on the game state.
     */
    private void fillSquares() {
        for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++) {
            int row = i / Board.DIMENSION;
            int col = i % Board.DIMENSION;
            Tile tile = game.getBoard().getTileAt(new Offset2D(row, col));
            JPanel square = tiles[row][col];
            fillSquaresWithImage(tile, square);
        }
    }

    /**
     * Fills a specific square with the image of the troop located at that tile.
     *
     * @param tile   The tile containing the troop.
     * @param square The square JPanel to fill with the image.
     */
    private void fillSquaresWithImage(Tile tile, JPanel square) {
        square.removeAll();
        if (tile.getTroop() != null && tile.getPlayer() != null) {
            String troopName = tile.getTroop().type().getName();
            char playerSide = tile.getPlayer().getPlayingSideString().charAt(0);
            String troopPath = playerSide + "-" + troopName;
            ImageIcon imageIcon = loadImageIcon("icons/" + troopPath + ".png");
            JLabel pieceLabel = new JLabel(imageIcon);
            square.add(pieceLabel);
        }
        square.revalidate();
        square.repaint();
    }

    /**
     * Loads an image icon from the specified path.
     *
     * @param path The path to the image file.
     * @return The loaded ImageIcon, or null if the image could not be loaded.
     */
    private ImageIcon loadImageIcon(String path) {
        ImageIcon icon = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                icon = new ImageIcon(bufferedImage);
            } else {
                logger.info("Resource not found: " + path);
            }
        } catch (IOException e) {
            logger.info("Reading image error: " + path);
            e.printStackTrace();
        }
        return icon;
    }

    /**
     * Fills a specific square with a given color.
     *
     * @param offset2D The position of the square.
     * @param color    The color to fill the square with.
     */
    public void fillSquareWithColor(Offset2D offset2D, Color color) {
        JPanel square = tiles[offset2D.getRow()][offset2D.getColumn()];
        square.setBackground(color);
        square.revalidate();
        square.repaint();
    }

    /**
     * Fills multiple squares given from ArrayList with a given color.
     *
     * @param positions The positions of the squares to fill.
     * @param color     The color to fill the squares with.
     */
    public void fillSquaresWithColor(ArrayList<Offset2D> positions, Color color) {
        for (Offset2D position : positions) {
            fillSquareWithColor(position, color);
        }
    }

    /**
     * Resets the colors of all squares on the board to their default colors.
     */
    public void resetSquaresColors() {
        for (int i = 0; i < Board.DIMENSION; i++) {
            for (int j = 0; j < Board.DIMENSION; j++) {
                Color squareColor = Color.DARK_GRAY;
                if (Offset2D.TRAP_OFFSET.contains(new Offset2D(i, j))) {
                    squareColor = Color.BLACK;
                }
                fillSquareWithColor(new Offset2D(i, j), squareColor);
            }
        }
    }


    /**
     * Executes a step move and updates the board.
     *
     * @param stepMove The step move to execute.
     */
    public void stepMovePiece(StepMove stepMove) {
        game.getBoard().makeStepMove(stepMove);
        fillSquares();
        resetSquaresColors();
    }

    /**
     * Executes a pull move and updates the board.
     *
     * @param pullMove The pull move to execute.
     */
    public void pullMovePieces(ComplexMove pullMove) {
        StepMove pullingPieceMove = new StepMove(pullMove.getFrom(), pullMove.getTo());
        stepMovePiece(pullingPieceMove);
        checkTraps();
        StepMove pulledPieceMove = new StepMove(pullMove.getMovedPieceFrom(), pullMove.getMovedPieceTo());
        stepMovePiece(pulledPieceMove);
    }

    /**
     * Executes a push move and updates the board.
     *
     * @param pushMove The push move to execute.
     */
    public void pushMovePieces(ComplexMove pushMove) {
        StepMove pushedPieceMove = new StepMove(pushMove.getMovedPieceFrom(), pushMove.getMovedPieceTo());
        stepMovePiece(pushedPieceMove);
        checkTraps();
        StepMove pushingPieceMove = new StepMove(pushMove.getFrom(), pushMove.getTo());
        stepMovePiece(pushingPieceMove);
    }

    /**
     * Switches the positions of two pieces on the board.
     *
     * @param position1 The first position.
     * @param position2 The second position.
     */
    public void switchPieces(Offset2D position1, Offset2D position2) {
        game.getBoard().switchTroops(position1, position2);
        fillSquares();
        resetSquaresColors();
    }

    /**
     * Handles resetting the board based on the current game mode.
     */
    public void handleModeReset() {
        resetSquaresColors();
        boolean isSilverBottom = client != null && game.getCurrentPlayer().getPlayerId() == 2;
        switch (currentMode) {
            case SWITCH ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), null, "SWITCH", isSilverBottom), Color.WHITE);
            case STEP ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), null, "STEP", isSilverBottom), Color.WHITE);
            case PULL ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), game.getEnemyPlayer(), "PULL", isSilverBottom), Color.WHITE);
            case PUSH ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), game.getEnemyPlayer(), "PUSH", isSilverBottom), Color.WHITE);
        }
    }

    /**
     * Gets the position of a given square on the board.
     *
     * @param square The square to get the position of.
     * @return The position of the square.
     */
    public Offset2D getPositionFromSquare(JPanel square) {
        for (int i = 0; i < Board.DIMENSION; i++) {
            for (int j = 0; j < Board.DIMENSION; j++) {
                if (tiles[i][j].equals(square)) {
                    return new Offset2D(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Gets the positions of all squares with the specified color.
     *
     * @param color The color to search for.
     * @return A list of positions of squares with the specified color.
     */
    public ArrayList<Offset2D> getPositionsOfSquaresWithColor(Color color) {
        ArrayList<Offset2D> positionsOfSquares = new ArrayList<>();
        for (int i = 0; i < Board.DIMENSION; i++) {
            for (int j = 0; j < Board.DIMENSION; j++) {
                Color squareColor = tiles[i][j].getBackground();
                if (squareColor == color) {
                    positionsOfSquares.add(new Offset2D(i, j));
                }
            }
        }
        return positionsOfSquares;
    }

    /**
     * Creates a mouse adapter for handling clicks on the board.
     *
     * @return The created mouse adapter.
     */
    private MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel square = (JPanel) e.getSource();
                Offset2D clickedPosition = getPositionFromSquare(square);
                if (clickedOnWrongSquare(square)) {
                    handleModeReset();
                    return;
                }
                ArrayList<Offset2D> positionArrayList = getPositionsOfSquaresWithColor(currentMode.getColor());
                switch (currentMode) {
                    case SWITCH -> handleSwitchMode(clickedPosition, positionArrayList);
                    case STEP -> {
                        if (game.getMovesLeft() < 1) break;
                        handleStepMode(clickedPosition, positionArrayList);
                    }
                    case PULL -> {
                        if (game.getMovesLeft() < 2) break;
                        handlePullMode(clickedPosition, positionArrayList);
                    }
                    case PUSH -> {
                        if (game.getMovesLeft() < 2) break;
                        handlePushMode(clickedPosition, positionArrayList);
                    }
                }
            }
        };
    }

    /**
     * Checks if a click was on an invalid square.
     *
     * @param square The square that was clicked.
     * @return True if the click was on an invalid square, false otherwise.
     */
    private boolean clickedOnWrongSquare(JPanel square) {
        Color color = square.getBackground();
        return color == Color.BLACK || color == Color.DARK_GRAY || color == currentMode.getColor();
    }

    /**
     * Handles the SWITCH game mode logic.
     *
     * @param squarePosition     The position of the clicked square.
     * @param selectedPositions  The positions currently selected.
     */
    private void handleSwitchMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            fillSquareWithColor(squarePosition, currentMode.getColor());
        } else if (selectedPositions.size() == 1) {
            switchPieces(selectedPositions.get(0), squarePosition);
            handleModeReset();
        }
    }

    /**
     * Handles the STEP game mode logic.
     * If client is created then after move is done the gameState will be sent to another player to update his board.
     *
     * @param squarePosition     The position of the clicked square.
     * @param selectedPositions  The positions currently selected.
     */
    private void handleStepMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            boolean isSilverBottom = client != null && game.getCurrentPlayer().getPlayerId() == 2;
            fillSquaresWithColor((ArrayList<Offset2D>) game.getBoard().getValidStepMovesByItselfForPosition(squarePosition, isSilverBottom).stream().map(StepMove::getTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 1) {
            stepMovePiece(new StepMove(selectedPositions.get(0), squarePosition));
            checkTraps();
            if (client != null)
                game.checkWinningOnline();
            else
                game.checkWinning();
            if (!game.isGameFinished()) {
                handleModeReset();
                game.decrementMovesLeft(1);
                if (client != null) {
                    GameState gameState = game.saveState(1, false);
                    try {
                        client.sendObjectToEnemy(gameState);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    /**
     * Handles the PULL game mode logic.
     * If client is created then after move is done the gameState will be sent to another player to update his board.
     *
     * @param squarePosition     The position of the clicked square.
     * @param selectedPositions  The positions currently selected.
     */
    private void handlePullMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            boolean isSilverBottom = client != null && game.getCurrentPlayer().getPlayerId() == 2;
            fillSquaresWithColor(game.getBoard().getPositionsOfPossiblePullingPieces(squarePosition, isSilverBottom), Color.WHITE);
        } else if (selectedPositions.size() == 1) {
            resetSquaresColors();
            selectedPositions.add(squarePosition);
            fillSquaresWithColor(selectedPositions, currentMode.getColor());
            boolean isSilverBottom = client != null && game.getCurrentPlayer().getPlayerId() == 2;
            fillSquaresWithColor((ArrayList<Offset2D>) game.getBoard().getValidPullMovesForPullerAndPulled(squarePosition, selectedPositions.get(0), isSilverBottom).stream().map(ComplexMove::getTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 2) {
            Offset2D pulledPiecePosition = null;
            Offset2D pullingPiecePosition = null;
            for (Offset2D position : selectedPositions) {
                if (game.getBoard().getPlayerAt(position).equals(game.getCurrentPlayer())) {
                    pullingPiecePosition = position;
                } else {
                    pulledPiecePosition = position;
                }
            }
            pullMovePieces(new ComplexMove(pullingPiecePosition, squarePosition, pulledPiecePosition, pullingPiecePosition));
            checkTraps();
            if (client != null)
                game.checkWinningOnline();
            else
                game.checkWinning();
            if (!game.isGameFinished()) {
                handleModeReset();
                game.decrementMovesLeft(2);
                if (client != null) {
                    GameState gameState = game.saveState(1, false);
                    try {
                        client.sendObjectToEnemy(gameState);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    /**
     * Handles the PUSH game mode logic.
     * If client is created then after move is done the gameState will be sent to another player to update his board.
     *
     * @param squarePosition     The position of the clicked square.
     * @param selectedPositions  The positions currently selected.
     */
    private void handlePushMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor(game.getBoard().getPositionsOfPossiblePushingPieces(squarePosition), Color.WHITE);
        } else if (selectedPositions.size() == 1) {
            resetSquaresColors();
            selectedPositions.add(squarePosition);
            fillSquaresWithColor(selectedPositions, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Offset2D>) game.getBoard().getValidPushMovesForPusherAndPushed(squarePosition, selectedPositions.get(0)).stream().map(ComplexMove::getMovedPieceTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 2) {
            Offset2D pushedPiecePosition = null;
            Offset2D pushingPiecePosition = null;
            for (Offset2D position : selectedPositions) {
                if (game.getBoard().getPlayerAt(position).equals(game.getCurrentPlayer())) {
                    pushingPiecePosition = position;
                } else {
                    pushedPiecePosition = position;
                }
            }
            pushMovePieces(new ComplexMove(pushingPiecePosition, pushedPiecePosition, pushedPiecePosition, squarePosition));
            checkTraps();
            if (client != null)
                game.checkWinningOnline();
            else
                game.checkWinning();
            if (!game.isGameFinished()) {
                handleModeReset();
                game.decrementMovesLeft(2);
                if (client != null) {
                    GameState gameState = game.saveState(1, false);
                    try {
                        client.sendObjectToEnemy(gameState);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    /**
     * Removes the troop at the specified position.
     *
     * @param position The position to remove the troop from.
     */
    public void removeTroopAt(Offset2D position) {
        game.getBoard().removeTroop(position);
        fillSquares();
        resetSquaresColors();
    }

    /**
     * Checks for troops on trap squares and removes them if they are not supported by friendly troops.
     */
    private void checkTraps() {
        for (Offset2D position : Offset2D.TRAP_OFFSET) {
            if (game.getBoard().getTileAt(position).getTroop() != null && !game.getBoard().isFriendlyTroopAround(position)) {
                stepMovePiece(new StepMove(position, position));
                removeTroopAt(position);
            }
        }
    }

    /**
     * Clicks on a random square with the specified color.
     *
     * @param preferTrap Whether to prefer trap squares.
     */
    public void clickOnRandomWhiteSquare(boolean preferTrap) {
        ArrayList<Offset2D> positions = getPositionsOfSquaresWithColor(Color.WHITE);
        Random random = new Random();

        Offset2D position = selectPosition(positions, preferTrap, random);

        JPanel square = tiles[position.getRow()][position.getColumn()];
        MouseEvent event = new MouseEvent(square, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 1, 1, 1, false);
        for (MouseListener listener : square.getMouseListeners()) {
            listener.mouseClicked(event);
        }
    }

    /**
     * Selects a position from the list of positions, optionally preferring trap positions.
     *
     * @param positions  The list of positions to choose from.
     * @param preferTrap Whether to prefer trap positions.
     * @param random     The Random instance to use for selection.
     * @return The selected position.
     */
    private Offset2D selectPosition(ArrayList<Offset2D> positions, boolean preferTrap, Random random) {
        if (positions.size() <= 1) {
            return positions.get(0);
        }

        if (preferTrap) {
            for (Offset2D onePosition : positions) {
                if (Offset2D.TRAP_OFFSET.contains(onePosition)) {
                    return onePosition;
                }
            }
        }

        Offset2D position;
        do {
            position = positions.get(random.nextInt(positions.size()));
        } while (preferTrap && Offset2D.TRAP_OFFSET.contains(position) && positions.size() > 1);

        return position;
    }

    public void setGame(GameModel game) {
        this.game = game;
        fillSquares();
        resetSquaresColors();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setGameMode(GameMode mode) {
        currentMode = mode;
    }

}
