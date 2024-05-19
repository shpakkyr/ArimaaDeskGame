package Client.View;

import Client.Model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ReplayControllerPanel extends JPanel{
    private final JButton nextMove;
    private final JButton prevMove;
    private final JButton returnButton;
    private BoardPanel boardPanel;
    private GameModel game;
    private Player player1,player2;
    private ArrayList<GameState> gameState = new ArrayList<GameState>();
    private JPanel replayControllerPanel;
    private int currentBoard = 0;
    private final JLabel timerLabel;
    private CountdownTimer timer = new CountdownTimer(10);
    public ReplayControllerPanel(GameModel game, GameView view) {
        this.game = game;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel playerTopName = createColoredLabel("Silver Player (" + game.getPlayer2().getPlayerName() + ")", Color.LIGHT_GRAY, 14, false);
        JPanel player2infoGroup = createGroupPanel(Color.BLACK);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
        player2infoGroup.add(playerTopName);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));

        timerLabel = createColoredLabel("00:00", Color.BLACK, 14, true);


        nextMove = new JButton("Next Move");
        nextMove.setMaximumSize(new Dimension(Integer.MAX_VALUE, nextMove.getMinimumSize().height));
        prevMove = new JButton("Prev Move");
        prevMove.setMaximumSize(new Dimension(Integer.MAX_VALUE, prevMove.getMinimumSize().height));
        returnButton = new JButton("Main Menu");
        returnButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, returnButton.getMinimumSize().height));

        replayControllerPanel = createGroupPanel(Color.WHITE);
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

        JLabel playerBottomName = createColoredLabel("Gold Player (" + game.getPlayer1().getPlayerName() + ")", Color.ORANGE, 14, false);
        JPanel player1infoGroup = createGroupPanel(Color.BLACK);
        player1infoGroup.add(playerBottomName);
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));

        add(player2infoGroup, BorderLayout.NORTH);
        add(replayControllerPanel, BorderLayout.CENTER);
        add(player1infoGroup, BorderLayout.SOUTH);

        nextMove.addActionListener(e -> {
            if(gameState.getLast() != gameState.get(currentBoard)) {
                currentBoard++;
                game.getBoard().populateBoardFrom2DString(gameState.get(currentBoard).boardState, player1, player2);
                boardPanel = new BoardPanel(game);
                boardPanel.setGame(game);
                view.changeCurrentPanel(boardPanel);

                timerLabel.setText(timer.formatTime((10*60*1000) - (gameState.get(currentBoard).remainingTime)));
            }else{
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
            }else{
                JOptionPane.showMessageDialog(
                        null,
                        "This is the first move in replay.",
                        "Prev Move",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        returnButton.addActionListener(e -> {
            view.init();
        });
    }

    public void loadSnapInfo(ArrayList<GameState> loadState){
        gameState = (ArrayList<GameState>) loadState.clone();

        player1 = new Player(1, gameState.get(currentBoard).player1.getPlayerName(), false);
        player2 = new Player(2, gameState.get(currentBoard).player1.getPlayerName(), false);

        game.getBoard().populateBoardFrom2DString(gameState.getLast().boardState, player1, player2);

        timerLabel.setText("Init arrangement");
    }


    private JLabel createColoredLabel(String text, Color color, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    private JPanel createGroupPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}
