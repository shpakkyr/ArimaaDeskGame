package Client.View;

import Client.Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import static Client.View.GameMode.STEP;

public class BoardPanel extends JPanel {
    private GameModel game;
    private GameMode currentMode = GameMode.NONE;
    private final JPanel[][] tiles;

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

    public void setGame(GameModel game) {
        this.game = game;
        fillSquares();
        resetSquaresColors();
    }

    private void fillSquares() {
        for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++) {
            int row = i / Board.DIMENSION;
            int col = i % Board.DIMENSION;
            Tile tile = game.getBoard().getTileAt(new Offset2D(row, col));
            JPanel square = tiles[row][col];
            fillSquaresWithImage(tile, square);
        }
    }

    private void fillSquaresWithImage(Tile tile, JPanel square) {
        square.removeAll();
        if (tile.getTroop() != null && tile.getPlayer() != null) {
            String troopName = tile.getTroop().getType().getName();
            char playerSide = tile.getPlayer().getPlayingSideString().charAt(0);
            String troopPath = playerSide + "-" + troopName;
            ImageIcon imageIcon = loadImageIcon("icons/" + troopPath + ".png");
            JLabel pieceLabel = new JLabel(imageIcon);
            square.add(pieceLabel);
        }
        square.revalidate();
        square.repaint();
    }

    private ImageIcon loadImageIcon(String path) {
        ImageIcon icon = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                icon = new ImageIcon(bufferedImage);
            } else {
                System.err.println("Resource not found: " + path);
            }
        } catch (IOException e) {
            System.err.println("Reading image error: " + path);
            e.printStackTrace();
        }
        return icon;
    }

    public void placeTroopAt(Troop troop, Offset2D offset2D) {
        game.getBoard().placeTroop(troop, offset2D);
        fillSquares();
        resetSquaresColors();
    }

    public void fillSquareWithColor(Offset2D offset2D, Color color) {
        JPanel square = tiles[offset2D.getRow()][offset2D.getColumn()];
        square.setBackground(color);
        square.revalidate();
        square.repaint();
    }

    public void fillSquaresWithColor(ArrayList<Offset2D> positions, Color color) {
        for (Offset2D position : positions) {
            fillSquareWithColor(position, color);
        }
    }

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

    public void setGameMode(GameMode mode) {
        currentMode = mode;
    }

    public void stepMovePiece(StepMove stepMove) {
        game.getBoard().makeStepMove(stepMove);
        fillSquares();
        resetSquaresColors();
    }

    public void pullMovePieces(PullMove pullMove) {
        StepMove pullingPieceMove = new StepMove(pullMove.getFrom(), pullMove.getTo());
        stepMovePiece(pullingPieceMove);
        checkTraps();
        StepMove pulledPieceMove = new StepMove(pullMove.getPulledPieceFrom(), pullMove.getPulledPieceTo());
        stepMovePiece(pulledPieceMove);
    }

    public void pushMovePieces(PushMove pushMove) {
        StepMove pushedPieceMove = new StepMove(pushMove.getPushedPieceFrom(), pushMove.getPushedPieceTo());
        stepMovePiece(pushedPieceMove);
        checkTraps();
        StepMove pushingPieceMove = new StepMove(pushMove.getFrom(), pushMove.getTo());
        stepMovePiece(pushingPieceMove);
    }

    public void switchPieces(Offset2D position1, Offset2D position2) {
        game.getBoard().switchTroops(position1, position2);
        fillSquares();
        resetSquaresColors();
    }

    public void handleModeReset() {
        resetSquaresColors();
        switch (currentMode) {
            case SWITCH ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), null, "SWITCH"), Color.WHITE);
            case STEP ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), null, "STEP"), Color.WHITE);
            case PULL ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), game.getEnemyPlayer(), "PULL"), Color.WHITE);
            case PUSH ->
                    fillSquaresWithColor(game.getBoard().getPositionsOfPlayersTroops(game.getCurrentPlayer(), game.getEnemyPlayer(), "PUSH"), Color.WHITE);
        }
    }

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

    private boolean clickedOnWrongSquare(JPanel square) {
        Color color = square.getBackground();
        return color == Color.BLACK || color == Color.DARK_GRAY || color == currentMode.getColor();
    }

    private void handleSwitchMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            fillSquareWithColor(squarePosition, currentMode.getColor());
        } else if (selectedPositions.size() == 1) {
            switchPieces(selectedPositions.get(0), squarePosition);
            handleModeReset();
        }
    }

    private void handleStepMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Offset2D>) game.getBoard().getValidStepMovesByItselfForPosition(squarePosition).stream().map(StepMove::getTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 1) {
            stepMovePiece(new StepMove(selectedPositions.get(0), squarePosition));
            checkTraps();
            game.checkWinning();
            if (!game.isGameFinished()) {
                handleModeReset();
                game.decrementMovesLeft(1);
            }
        }
    }

    private void handlePullMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor(game.getBoard().getPositionsOfPossiblePullingPieces(squarePosition), Color.WHITE);
        } else if (selectedPositions.size() == 1) {
            resetSquaresColors();
            selectedPositions.add(squarePosition);
            fillSquaresWithColor(selectedPositions, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Offset2D>) game.getBoard().getValidPullMovesForPullerAndPulled(squarePosition, selectedPositions.get(0)).stream().map(PullMove::getTo).collect(Collectors.toList()), Color.WHITE);
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
            pullMovePieces(new PullMove(pullingPiecePosition, squarePosition, pulledPiecePosition, pullingPiecePosition));
            checkTraps();
            game.checkWinning();
            if (!game.isGameFinished()) {
                handleModeReset();
                game.decrementMovesLeft(2);
            }
        }
    }

    private void handlePushMode(Offset2D squarePosition, ArrayList<Offset2D> selectedPositions) {
        if (selectedPositions.size() == 0) {
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor(game.getBoard().getPositionsOfPossiblePullingPieces(squarePosition), Color.WHITE);
        } else if (selectedPositions.size() == 1) {
            resetSquaresColors();
            selectedPositions.add(squarePosition);
            fillSquaresWithColor(selectedPositions, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Offset2D>) game.getBoard().getValidPushMovesForPusherAndPushed(squarePosition, selectedPositions.get(0)).stream().map(PushMove::getPushedPieceTo).collect(Collectors.toList()), Color.WHITE);
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
            pushMovePieces(new PushMove(pushingPiecePosition, pushedPiecePosition, pushedPiecePosition, squarePosition));
            checkTraps();
            game.checkWinning();
            if (!game.isGameFinished()) {
                handleModeReset();
                game.decrementMovesLeft(2);
            }
        }
    }

    public void removeTroopAt(Offset2D position) {
        game.getBoard().removeTroop(position);
        fillSquares();
        resetSquaresColors();
    }

    private void checkTraps() {
        for (Offset2D position : Offset2D.TRAP_OFFSET) {
            if (game.getBoard().getTileAt(position).getTroop() != null && !game.getBoard().isFriendlyTroopAround(position)) {
                stepMovePiece(new StepMove(position, position));
                removeTroopAt(position);
            }
        }
    }

    public void clickOnRandomWhiteSquare(boolean preferTrap) {
        ArrayList<Offset2D> positions = getPositionsOfSquaresWithColor(Color.WHITE);
        Random random = new Random();
        Offset2D position = positions.get(random.nextInt(positions.size()));
        if (positions.size() > 1) {
            if (preferTrap) {
                for (Offset2D onePosition : positions) {
                    if (Offset2D.TRAP_OFFSET.contains(onePosition)) {
                        position = onePosition;
                    }
                }
            } else {
                while (Offset2D.TRAP_OFFSET.contains(position)) {
                    position = positions.get(random.nextInt(positions.size()));
                }
            }
        }
        JPanel square = tiles[position.getRow()][position.getColumn()];
        MouseEvent event = new MouseEvent(square, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 1, 1, 1, false);
        for (MouseListener listener : square.getMouseListeners()) {
            listener.mouseClicked(event);
        }
    }
}
