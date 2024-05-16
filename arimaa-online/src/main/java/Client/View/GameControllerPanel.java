package Client.View;

import Client.Model.Board;
import Client.Model.GameListener;
import Client.Model.GameModel;
import Client.Model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;

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
    private final BoardPanel boardPanel;
    private final GameModel game;
    private final JPanel gameControllerPanel;

    public GameControllerPanel(GameModel game, BoardPanel boardPanel) {
        this.game = game;
        this.boardPanel = boardPanel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel playerTopName = createColoredLabel("Silver Player (" + game.getPlayer2().getPlayerName() + ")", Color.LIGHT_GRAY, 14, false);
        JPanel player2infoGroup = createGroupPanel(Color.BLACK);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
        player2infoGroup.add(playerTopName);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));

        turnIndicator = new JLabel("Gold Player's Turn");
        turnIndicator.setForeground(Color.BLACK);
        movesLeftLabel = new JLabel("Arrange your pieces");

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

        finishButton = new JButton("Finish");
        finishButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, finishButton.getMinimumSize().height));
        giveUpButton = new JButton("Give Up");
        giveUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, giveUpButton.getMinimumSize().height));
        saveButton = new JButton("Save Game State");
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveButton.getMinimumSize().height));

        gameControllerPanel = createGroupPanel(Color.WHITE);
        gameControllerPanel.setLayout(new BoxLayout(gameControllerPanel, BoxLayout.Y_AXIS));
        gameControllerPanel.add(Box.createVerticalGlue());
        gameControllerPanel.add(turnIndicator);
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
        gameControllerPanel.add(Box.createVerticalGlue());

        JLabel playerBottomName = createColoredLabel("Gold Player (" + game.getPlayer1().getPlayerName() + ")", Color.ORANGE, 14, false);
        JPanel player1infoGroup = createGroupPanel(Color.BLACK);
        player1infoGroup.add(playerBottomName);
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));

        add(player2infoGroup, BorderLayout.NORTH);
        add(gameControllerPanel, BorderLayout.CENTER);
        add(player1infoGroup, BorderLayout.SOUTH);

        setTurnFormatting();
        if (game.getPhase() >= 3){
            onMovesLeftChanged(game.getMovesLeft());
        }

        finishButton.addActionListener(e -> {
            boardPanel.resetSquaresColors();
            boardPanel.setGameMode(GameMode.NONE);
            boardPanel.handleModeReset();
            game.incrementPhase();
            setTurnFormatting();

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
            onGameEnded(game.getWinner());
        });
    }

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
        }
    }

    @Override
    public void onGameEnded(Player winner) {
        boardPanel.setGameMode(GameMode.NONE);
        boardPanel.handleModeReset();
        noneButton.setVisible(false);
        stepButton.setVisible(false);
        pushButton.setVisible(false);
        pullButton.setVisible(false);
        finishButton.setVisible(false);
        giveUpButton.setVisible(false);
        movesLeftLabel.setVisible(false);
        turnIndicator.setText(winner.getPlayerName() + " won!");
        showWinnerPopup(winner);
    }

    private void setTurnFormatting(){
        Color turnColor;
        if (game.getPhase() % 2 == 1){
            turnColor = Color.ORANGE;
            turnIndicator.setText("Gold Player's Turn");
        } else {
            turnColor = Color.LIGHT_GRAY;
            turnIndicator.setText("Silver Player's Turn");
        }
        gameControllerPanel.setBackground(turnColor);
        if (game.getPhase() <= 2){
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
            saveButton.setVisible(true);
        }
        if (game.getCurrentPlayer().isComputer()){
            finishButton.setVisible(false);
            giveUpButton.setVisible(false);
            switchButton.setVisible(false);
            noneButton.setVisible(false);
            stepButton.setVisible(false);
            pushButton.setVisible(false);
            pullButton.setVisible(false);
        }

    }

    public void showWinnerPopup(Player winner) {
        String message = winner.getPlayerName() + " won. Congratulations!";
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
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
