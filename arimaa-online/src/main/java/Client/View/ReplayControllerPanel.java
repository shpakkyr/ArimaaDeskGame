package Client.View;

import Client.Model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * ReplayControllerPanel allows users to control the replay of a previously saved game.
 * Users can navigate through the moves, see the current state of the game board, and return to the main menu.
 */
public class ReplayControllerPanel extends JPanel{
    private BoardPanel boardPanel;
    private final GameModel game;
    private Player player1,player2;
    private ArrayList<GameState> gameState = new ArrayList<GameState>();
    private int currentBoard = 0;
    private final JLabel timerLabel;
    private final CountdownTimer timer = new CountdownTimer(10);

    /**
     * Constructs a new ReplayControllerPanel with the specified game model and view.
     *
     * @param game The game model.
     * @param view The game view.
     */
    public ReplayControllerPanel(GameModel game, GameView view) {
        this.game = game;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Player 2 (Silver) info at the top
        JLabel playerTopName = createColoredLabel("Silver Player (" + game.getPlayer2().getPlayerName() + ")", Color.LIGHT_GRAY, 14, false);
        JPanel player2infoGroup = createGroupPanel(Color.BLACK);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
        player2infoGroup.add(playerTopName);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));

        // Timer label
        timerLabel = createColoredLabel("00:00", Color.BLACK, 14, true);

        // Control buttons
        JButton nextMove = new JButton("Next Move");
        nextMove.setMaximumSize(new Dimension(Integer.MAX_VALUE, nextMove.getMinimumSize().height));
        JButton prevMove = new JButton("Prev Move");
        prevMove.setMaximumSize(new Dimension(Integer.MAX_VALUE, prevMove.getMinimumSize().height));
        JButton returnButton = new JButton("Main Menu");
        returnButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, returnButton.getMinimumSize().height));

        // Center control panel
        JPanel replayControllerPanel = createGroupPanel(Color.WHITE);
        replayControllerPanel.setLayout(new BoxLayout(replayControllerPanel, BoxLayout.Y_AXIS));
        replayControllerPanel.add(Box.createVerticalGlue());
        replayControllerPanel.add(timerLabel);
        replayControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        replayControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        replayControllerPanel.add(nextMove);
        replayControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        replayControllerPanel.add(prevMove);
        replayControllerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        replayControllerPanel.add(returnButton);
        replayControllerPanel.add(Box.createVerticalGlue());

        // Player 1 (Gold) info at the bottom
        JLabel playerBottomName = createColoredLabel("Gold Player (" + game.getPlayer1().getPlayerName() + ")", Color.ORANGE, 14, false);
        JPanel player1infoGroup = createGroupPanel(Color.BLACK);
        player1infoGroup.add(playerBottomName);
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));

        // Adding components to the panel
        add(player2infoGroup, BorderLayout.NORTH);
        add(replayControllerPanel, BorderLayout.CENTER);
        add(player1infoGroup, BorderLayout.SOUTH);

        // Action listeners for the control buttons
        nextMove.addActionListener(e -> {
            if(gameState.getLast() != gameState.get(currentBoard)) {
                currentBoard++;
                game.getBoard().populateBoardFrom2DString(gameState.get(currentBoard).boardState, player1, player2);
                boardPanel = new BoardPanel(game);
                boardPanel.setGame(game);
                view.changeCurrentPanel(boardPanel);

                timerLabel.setText(timer.formatTime((10*60*1000) - (gameState.get(currentBoard).remainingTime)));
            }else{//If current board is the last board in gameState then show info message
                JOptionPane.showMessageDialog(
                        null,
                        "This is the last move in replay.",
                        "Next Move",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        prevMove.addActionListener(e -> {
            if(currentBoard > 0) {
                currentBoard--;
                game.getBoard().populateBoardFrom2DString(gameState.get(currentBoard).boardState, player1, player2);
                boardPanel = new BoardPanel(game);
                boardPanel.setGame(game);
                view.changeCurrentPanel(boardPanel);

                if(currentBoard == 0){
                    timerLabel.setText("Init arrangement");
                }else {
                    timerLabel.setText(timer.formatTime((10 * 60 * 1000) - (gameState.get(currentBoard).remainingTime)));
                }
            }else{//If current board is the first board in gameState then show info message
                JOptionPane.showMessageDialog(
                        null,
                        "This is the first move in replay.",
                        "Prev Move",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        returnButton.addActionListener(e -> {
            view.closeWindow();
            view.init();
        });
    }

    /**
     * Loads the game state snapshots for the replay.
     *
     * @param loadState The list of game state snapshots.
     */
    public void loadSnapInfo(ArrayList<GameState> loadState){
        gameState = (ArrayList<GameState>) loadState.clone();

        player1 = new Player(1, gameState.get(currentBoard).player1.getPlayerName(), false);
        player2 = new Player(2, gameState.get(currentBoard).player1.getPlayerName(), false);

        game.getBoard().populateBoardFrom2DString(gameState.get(currentBoard).boardState, player1, player2);

        timerLabel.setText("Init arrangement");
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
