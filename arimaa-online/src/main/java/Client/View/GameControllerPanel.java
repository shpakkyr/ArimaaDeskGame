package Client.View;

import Client.Controller.*;
import Client.Model.*;
import Server.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GameControllerPanel displays information about current turn and controls for individual moves.
 */
public class GameControllerPanel extends JPanel implements GameListener {
    private final JLabel turnIndicator;
    private final JLabel movesLeftLabel;
    private final JRadioButton switchButton;
    private final JRadioButton noneButton;
    private final JRadioButton stepButton;
    private final JRadioButton pullButton;
    private final JRadioButton pushButton;
    private final JButton finishButton;
    private final JButton giveUpButton;
    private final JButton saveButton;
    private final JButton compButton;
    private final JButton menuButton;

    private final BoardPanel boardPanel;
    private final GameModel game;
    private ArrayList<GameState> gameState = new ArrayList<GameState>();
    private final JPanel gameControllerPanel;
    private CountdownTimer timer;
    private final boolean vsComputer;
    private final JLabel timerLabel;
    private final int playerId;
    private Client client;

    /**
     * Constructs a new GameControllerPanel with the specified game model and view.
     *
     * @param game The game model.
     * @param boardPanel The panel with a board.
     * @param vsComputer boolean to determine the mode of the game.
     * @param playerId Id of a player
     * @param view The game view.
     */

    public GameControllerPanel(GameModel game, BoardPanel boardPanel, boolean vsComputer, int playerId, GameView view) {
        this.game = game;
        this.boardPanel = boardPanel;
        this.vsComputer = vsComputer;
        this.playerId = playerId;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel player1infoGroup;
        JPanel player2infoGroup;

        // NORTH - player 2 info
        if (playerId == 1) {
            JLabel playerTopName = createColoredLabel("Silver Player (" + game.getPlayer2().getPlayerName() + ")", Color.LIGHT_GRAY, 14, false);
            player2infoGroup = createGroupPanel(Color.BLACK);
            player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
            player2infoGroup.add(playerTopName);
            player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        } else if (playerId == 2){
            JLabel playerTopName = createColoredLabel("Gold Player (" + game.getPlayer2().getPlayerName() + ")", Color.ORANGE, 14, false);
            player2infoGroup = createGroupPanel(Color.BLACK);
            player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
            player2infoGroup.add(playerTopName);
            player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        } else {
            JLabel playerTopName = createColoredLabel("Silver Player (" + game.getPlayer2().getPlayerName() + ")", Color.LIGHT_GRAY, 14, false);
            player2infoGroup = createGroupPanel(Color.BLACK);
            player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
            player2infoGroup.add(playerTopName);
            player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        //CENTER - timer, turn info and control buttons
        turnIndicator = new JLabel("Gold Player's Turn");
        turnIndicator.setForeground(Color.BLACK);

        timerLabel = createColoredLabel("10:00", Color.BLACK, 14, true);

        movesLeftLabel = new JLabel("Arrange your pieces");

        //Radio buttons
        switchButton = new JRadioButton(GameMode.SWITCH.getModeName());
        switchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, switchButton.getMinimumSize().height));
        switchButton.setOpaque(true);
        switchButton.setBackground(GameMode.SWITCH.getColor());
        noneButton = new JRadioButton(GameMode.NONE.getModeName());
        noneButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, noneButton.getMinimumSize().height));
        noneButton.setOpaque(true);
        noneButton.setBackground(GameMode.NONE.getColor());
        stepButton = new JRadioButton(GameMode.STEP.getModeName());
        stepButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, stepButton.getMinimumSize().height));
        stepButton.setOpaque(true);
        stepButton.setBackground(GameMode.STEP.getColor());
        pushButton = new JRadioButton(GameMode.PUSH.getModeName());
        pushButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pushButton.getMinimumSize().height));
        pushButton.setOpaque(true);
        pushButton.setBackground(GameMode.PUSH.getColor());
        pullButton = new JRadioButton(GameMode.PULL.getModeName());
        pullButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pullButton.getMinimumSize().height));
        pullButton.setOpaque(true);
        pullButton.setBackground(GameMode.PULL.getColor());
        switchButton.setVisible(true);
        noneButton.setVisible(true);
        stepButton.setVisible(true);
        pushButton.setVisible(true);
        pullButton.setVisible(true);
        noneButton.setSelected(true);
        ButtonGroup actionTypeGroup = new ButtonGroup();
        actionTypeGroup.add(switchButton);
        actionTypeGroup.add(noneButton);
        actionTypeGroup.add(stepButton);
        actionTypeGroup.add(pushButton);
        actionTypeGroup.add(pullButton);
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setOpaque(false);
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.add(switchButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(noneButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(stepButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pushButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pullButton);

        //Buttons for finishing turn, saving game to a file, giving up and move from computer during single player mode
        finishButton = new JButton("Finish");
        finishButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, finishButton.getMinimumSize().height));
        giveUpButton = new JButton("Give Up");
        giveUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, giveUpButton.getMinimumSize().height));
        saveButton = new JButton("Save Game");
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveButton.getMinimumSize().height));
        compButton = new JButton("PC Move");
        compButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, compButton.getMinimumSize().height));
        menuButton = new JButton("Main Menu");
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, compButton.getMinimumSize().height));
        compButton.setVisible(false);
        menuButton.setVisible(false);


        //CENTER constructor
        gameControllerPanel = createGroupPanel(Color.WHITE);
        gameControllerPanel.setLayout(new BoxLayout(gameControllerPanel, BoxLayout.Y_AXIS));
        gameControllerPanel.add(Box.createVerticalGlue());
        gameControllerPanel.add(turnIndicator);
        gameControllerPanel.add(timerLabel);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(movesLeftLabel);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(radioButtonsPanel);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(finishButton);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(giveUpButton);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(saveButton);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(compButton);
        gameControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControllerPanel.add(menuButton);
        gameControllerPanel.add(Box.createVerticalGlue());

        //SOUTH - player 1 info
        if (playerId == 1) {
            JLabel playerBottomName = createColoredLabel("Gold Player (" + game.getPlayer1().getPlayerName() + ")", Color.ORANGE, 14, false);
            player1infoGroup = createGroupPanel(Color.BLACK);
            player1infoGroup.add(playerBottomName);
            player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
            player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));
        } else if (playerId == 2){
            JLabel playerBottomName = createColoredLabel("Silver Player (" + game.getPlayer1().getPlayerName() + ")", Color.LIGHT_GRAY, 14, false);
            player1infoGroup = createGroupPanel(Color.BLACK);
            player1infoGroup.add(playerBottomName);
            player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
            player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));
        } else {
            JLabel playerBottomName = createColoredLabel("Gold Player (" + game.getPlayer1().getPlayerName() + ")", Color.ORANGE, 14, false);
            player1infoGroup = createGroupPanel(Color.BLACK);
            player1infoGroup.add(playerBottomName);
            player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
            player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));
        }

        //General constructor
        add(player2infoGroup, BorderLayout.NORTH);
        add(gameControllerPanel, BorderLayout.CENTER);
        add(player1infoGroup, BorderLayout.SOUTH);

        //Show relevant buttons during turn
        setTurnFormatting();
        if (game.getPhase() >= 3){
            onMovesLeftChanged(game.getMovesLeft());
        }

        //Action listeners for all buttons
        finishButton.addActionListener(e -> {
            if(game.getPhase() > 1 && game.getMovesLeft() == 4) {
                boardSnap();
            }
            boardPanel.resetSquaresColors();
            boardPanel.setGameMode(GameMode.NONE);
            boardPanel.handleModeReset();
            game.incrementPhase();
            setTurnFormatting();
            if (client != null) {
                disableButtons();
                GameState gameState = game.saveState(timer.getRemainingTime(), vsComputer);
                try {
                    client.sendObjectToEnemy(gameState);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        switchButton.addActionListener(e -> {
            boardPanel.setGameMode(GameMode.SWITCH);
            boardPanel.handleModeReset();
        });
        noneButton.addActionListener(e -> {
            boardPanel.setGameMode(GameMode.NONE);
            boardPanel.handleModeReset();
        });
        stepButton.addActionListener(e -> {
            boardPanel.setGameMode(GameMode.STEP);
            boardPanel.handleModeReset();
        });
        pullButton.addActionListener(e -> {
            boardPanel.setGameMode(GameMode.PULL);
            boardPanel.handleModeReset();
        });
        pushButton.addActionListener(e -> {
            boardPanel.setGameMode(GameMode.PUSH);
            boardPanel.handleModeReset();
        });
        giveUpButton.addActionListener(e -> {
            game.endGameGiveUp();
        });

        compButton.addActionListener(e -> {
            //computer does not change position of his figures
            if (game.getPhase() <= 2) {
                finishButton.doClick();
                return;
            }

            //algorithm to make computer spend all moves per turn
            if (noneButton.isSelected()) {
                compNoneSelect(game.getMovesLeft() >= 2);
            } else {
                compMoveSelect();
            }
        });

        saveButton.addActionListener(e -> saveGame());
        menuButton.addActionListener( e ->{
            view.closeWindow();
            Launcher.main(new String[0]);
        });
    }

    /**
     * Method disables buttons for a player when he finished the move.
     * Applies only for online multiplayer.
     */
    public void disableButtons() {
        switchButton.setVisible(false);
        noneButton.setVisible(false);
        stepButton.setVisible(false);
        pushButton.setVisible(false);
        pullButton.setVisible(false);
        finishButton.setVisible(false);
        movesLeftLabel.setVisible(false);
        saveButton.setVisible(false);
        compButton.setVisible(false);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Method to save the current game state to a file chosen by the user.
     * The method pauses the game timer, opens a file chooser dialog for the user
     * to select the save location, and then saves the game state to the selected file.
     * After saving, the timer is resumed with the remaining time.
     */
    public void saveGame() {
        timer.pauseTimer();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game State");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            Saver.saveGame(gameState, fileToSave);
        }

        startTimerWithRemainingTime(timer.getRemainingTime());
    }

    /**
     * Method to load the game state from a provided list of GameState objects.
     * This method clones the provided list and assigns it to the gameState variable.
     *
     * @param loadState An ArrayList of GameState objects to be loaded into the game.
     */
    protected void loadSnapInfo(ArrayList<GameState> loadState){
        gameState = (ArrayList<GameState>) loadState.clone();
    }

    /**
     * Method to capture the current state of the board and save it to the gameState list for saving game.
     */
    protected void boardSnap(){
        gameState.add(game.saveState(timer.getRemainingTime(), vsComputer));
    }

    /**
     * Method to start the game timer with the specified remaining time from a save file.
     *
     * @param remainingTime The remaining time in milliseconds.
     */
    protected void startTimerWithRemainingTime(long remainingTime) {
        timer = new CountdownTimer((int) (remainingTime / 1000 / 60));
        timer.setRemainingTime(remainingTime);
        timer.setOnTimerEnd(this::onTimerEnd);

        timer.setOnTick(() -> SwingUtilities.invokeLater(() -> timerLabel.setText(timer.getTimeString())));
        timer.start();
    }

    /**
     * Method to handle the end of the timer. It captures the board state, resets color of tiles,
     * increments the game phase, and gives a turn to another player.
     */
    private void onTimerEnd(){
        boardSnap();
        boardPanel.resetSquaresColors();
        boardPanel.setGameMode(GameMode.NONE);
        boardPanel.handleModeReset();
        game.incrementPhase();
        setTurnFormatting();
    }

    /**
     * Method to start the game timer with the specified duration in minutes.
     *
     * @param minutes The duration of timer in minutes.
     */
    private void startTimer(int minutes) {
        if (timer != null) {
            timer.stopAndLogTime();
        }
        timer = new CountdownTimer(minutes);
        timer.setOnTimerEnd(this::onTimerEnd);
        timer.setOnTick(() -> SwingUtilities.invokeLater(() -> timerLabel.setText(timer.getTimeString())));
        timer.start();
    }

    /**
     * Implements onMovesLeft fired after each move (hides push/pull if not enough moves left)
     * Makes a snapshot of a board for save file on every change of amount of turns.
     * @param movesLeft Number of moves left in the current turn.
     */
    @Override
    public void onMovesLeftChanged(int movesLeft) {
        if(movesLeft > 0){
            if (movesLeft < 2){
                if (pushButton.isSelected() || pullButton.isSelected()){
                    boardPanel.setGameMode(GameMode.NONE);
                    boardPanel.handleModeReset();
                }
                pushButton.setVisible(false);
                pullButton.setVisible(false);
                movesLeftLabel.setText(movesLeft + " move left");
            } else {
                movesLeftLabel.setText(movesLeft + " moves left");
            }
        } else {
            boardPanel.resetSquaresColors();
            boardPanel.setGameMode(GameMode.NONE);
            boardPanel.handleModeReset();
            game.incrementPhase();
            setTurnFormatting();
            if (client != null) {
                disableButtons();
                GameState gameState = game.saveState(timer.getRemainingTime(), false);
                try {
                    client.sendObjectToEnemy(gameState);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        boardSnap();
    }

    /**
     * Implements the onGameEnded by hiding game controls and displaying the winner.
     * @param winner The winner player of the game.
     */
    @Override
    public void onGameEnded(Player winner) {
        boardSnap();
        timer.stopAndLogTime();
        timerLabel.setVisible(false);
        boardPanel.setGameMode(GameMode.NONE);
        boardPanel.handleModeReset();
        noneButton.setVisible(false);
        stepButton.setVisible(false);
        pushButton.setVisible(false);
        pullButton.setVisible(false);
        finishButton.setVisible(false);
        giveUpButton.setVisible(false);
        movesLeftLabel.setVisible(false);
        compButton.setVisible(false);
        saveButton.setVisible(true);
        menuButton.setVisible(true);
        if (client != null) {
            try {
                client.sendMessageToServer("finishCommunicationWithServer");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        turnIndicator.setText(winner.getPlayerName() + " won!");
        showWinnerPopup(winner);
    }

    /**
     * Method to set the turn formatting, including updating the background color and visibility of controls based on the current phase and game mode.
     * Starts timer for each new turn and changes theme for every player new turn.
     */
    public void setTurnFormatting(){
        Color turnColor;
        if (game.getPhase() % 2 == 1){
            turnColor = Color.ORANGE;
            turnIndicator.setText("Gold Player's Turn");
        } else {
            turnColor = Color.LIGHT_GRAY;
            turnIndicator.setText("Silver Player's Turn");
        }

        startTimer(10);

        gameControllerPanel.setBackground(turnColor);

        if (game.getPhase() <= 2){
            if (playerId == game.getPhase() || playerId == 0) {
                movesLeftLabel.setText("Arrange your pieces");
                switchButton.setVisible(true);
                noneButton.setVisible(true);
                stepButton.setVisible(false);
                pushButton.setVisible(false);
                pullButton.setVisible(false);
                noneButton.setSelected(true);
                finishButton.setVisible(true);
                giveUpButton.setVisible(false);
                saveButton.setVisible(false);
            }
            else {
                giveUpButton.setVisible(false);
                disableButtons();
            }
        } else {
            movesLeftLabel.setText("4 moves left");
            switchButton.setVisible(false);
            noneButton.setVisible(true);
            stepButton.setVisible(true);
            pushButton.setVisible(true);
            pullButton.setVisible(true);
            noneButton.setSelected(true);
            finishButton.setVisible(true);
            giveUpButton.setVisible(true);
            saveButton.setVisible(playerId == 0);
            compButton.setVisible(false);

        }
        if (game.getCurrentPlayer().isComputer()){
            finishButton.setVisible(false);
            giveUpButton.setVisible(false);
            switchButton.setVisible(false);
            noneButton.setVisible(false);
            stepButton.setVisible(false);
            pushButton.setVisible(false);
            pullButton.setVisible(false);
            saveButton.setVisible(false);
            compButton.setVisible(true);
        }

    }

    /**
     * Method to handle the case when noneButton is selected during the computer's turn (means computer did not make any turn yet).
     *Checks if any figure can make complex move, otherwise make a random step.
     * @param complexMove A boolean indicating whether the move is complex (requires multiple steps).
     */
    private void compNoneSelect(boolean complexMove) {
        boolean canPull = !game.getBoard().canPullPositions(game.getEnemyPlayer()).isEmpty();
        boolean canPush = !game.getBoard().canPullPositions(game.getEnemyPlayer()).isEmpty();

        if (complexMove) {
            if (canPush) {
                pushButton.doClick();
            } else if (canPull) {
                pullButton.doClick();
            } else {
                stepButton.doClick();
            }
        } else {
            stepButton.doClick();
        }
    }

    /**
     * Method to handle the computer's tile selection for move.
     */
    private void compMoveSelect() {
        int whiteSquares = boardPanel.getPositionsOfSquaresWithColor(Color.WHITE).size();

        if (whiteSquares > 0) {
            boolean isPushSelected = pushButton.isSelected();
            boardPanel.clickOnRandomWhiteSquare(isPushSelected);
        } else {
            finishButton.doClick();
        }
    }

    /**
     * Shows the winner popup with the name of the player who won.
     * @param winner The winner.
     */
    protected void showWinnerPopup(Player winner) {
        String message = winner.getPlayerName() + " won!";
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method to create a colored label with specified text, color, font size, and boldness.
     *
     * @param text The text to display on the label.
     * @param color The color of the text.
     * @param fontSize The size of the font.
     * @param bold Whether the font should be bold.
     * @return A JLabel with the specified properties.
     */
    private JLabel createColoredLabel(String text, Color color, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    /**
     * Method to create a JPanel with a specified background color.
     *
     * @param color The background color of the panel.
     * @return A JPanel with the specified background color.
     */
    private JPanel createGroupPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}
