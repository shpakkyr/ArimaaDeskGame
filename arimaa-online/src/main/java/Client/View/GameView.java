package Client.View;

import Client.Controller.Loader;
import Client.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class GameView extends JPanel implements Runnable{
    private static JFrame window;
    private static JPanel currentPanel;
    private static JPanel currentRightPanel;
    private GameModel game;
    private BoardPanel boardPanel;
    private ReplayControllerPanel replayPanel;
    private GameControllerPanel controlPanel;
    final int FPS = 60;
    Thread gameThread;
    private final Board board = new Board();

    public GameView(GameModel game) {
        this.game = game;
    }

    public void init() {
        window = new JFrame("Arimaa Game");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setResizable(false);

        WelcomePanel welcomePanel = new WelcomePanel(game, this);
        window.add(welcomePanel, BorderLayout.CENTER);
        currentPanel = welcomePanel;

        currentRightPanel = new JPanel();
        window.add(currentRightPanel, BorderLayout.EAST);

        window.setSize(1050, 650);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    private void startNewGame(String player1name, String player2name, boolean vsComputer){
        Player player1 = new Player(1, player1name, false);
        Player player2 = new Player(2, player2name, vsComputer);
        game.setPlayers(player1, player2);
        game.getBoard().populateBoardFrom2DString(GameModel.DEFAULT_BOARD, player1, player2);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        controlPanel = new GameControllerPanel(game, boardPanel, vsComputer);
        game.setGameListener(controlPanel);
        currentRightPanel = controlPanel;
        changeRightPanel(currentRightPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void continueGame(ArrayList<GameState> gameState) {
        Player player1 = new Player(1, gameState.getLast().player1.getPlayerName(), false);
        Player player2 = new Player(2, gameState.getLast().player1.getPlayerName(), gameState.getFirst().vsComputer);
        game.setPlayers(player1, player2);
        game.getBoard().populateBoardFrom2DString(gameState.getLast().boardState, player1, player2);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        controlPanel = new GameControllerPanel(game, boardPanel, gameState.getFirst().vsComputer);
        game.setGameListener(controlPanel);
        currentRightPanel = controlPanel;
        changeRightPanel(currentRightPanel);
        controlPanel.startTimerWithRemainingTime(gameState.getLast().remainingTime);
        controlPanel.loadSnapInfo(gameState);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void reviewGame(ArrayList<GameState> gameState) {
        Player player1 = new Player(1, gameState.getLast().player1.getPlayerName(), false);
        Player player2 = new Player(2, gameState.getLast().player2.getPlayerName(), false);
        game.setPlayers(player1, player2);
        game.getBoard().populateBoardFrom2DString(gameState.getFirst().boardState, player1, player2);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        replayPanel = new ReplayControllerPanel(game,this);
        currentRightPanel = replayPanel;
        changeRightPanel(currentRightPanel);
        replayPanel.loadSnapInfo(gameState);
    }

    public void loadSave(boolean vsComputer) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            ArrayList<GameState> gameState = Loader.loadGame(fileToLoad);

            if (gameState != null && !gameState.getLast().isGameFinished && vsComputer == gameState.getFirst().vsComputer) {
                game.loadState(gameState);
                continueGame(gameState);
            }else if(gameState != null && vsComputer != gameState.getFirst().vsComputer && !vsComputer) {
                JOptionPane.showMessageDialog(
                        null,
                        "The save file is meant for single player mode.",
                        "Load Game",
                        JOptionPane.WARNING_MESSAGE
                );
            }else if(gameState != null && vsComputer != gameState.getFirst().vsComputer && vsComputer){
                JOptionPane.showMessageDialog(
                        null,
                        "The save file is meant for local multiplayer player mode.",
                        "Load Game",
                        JOptionPane.WARNING_MESSAGE
                );
            }else{
                JOptionPane.showMessageDialog(
                        null,
                        "The save file is corrupted or the game is only available for replay.",
                        "Load Game",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public void loadReplay() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Replay");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            ArrayList<GameState> gameState = Loader.loadGame(fileToLoad);

            if (gameState != null && gameState.getLast().isGameFinished) {
                game.loadReplayState(gameState);
                reviewGame(gameState);
            }else{
                JOptionPane.showMessageDialog(
                        null,
                        "The save file is corrupted or the game is not finished.",
                        "Load Replay",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public void showNewGameDialog(JFrame parentFrame, boolean vsComputer) {
        String gameType = vsComputer ? "single-player" : "multiplayer";
        JDialog newGameDialog = new JDialog(parentFrame, "New " + gameType + " game", true);
        newGameDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel player1Label = new JLabel("Gold Player:");
        JTextField player1TextField = new JTextField(20);
        player1TextField.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() + str.length() <= 20 && !str.contains(" ")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        JLabel player2Label = new JLabel("Silver Player:");
        JTextField player2TextField = new JTextField(20);
        player1TextField.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() + str.length() <= 20 && !str.contains(" ")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        if (vsComputer) {
            player2TextField.setText("Computer");
            player2TextField.setEditable(false);
        }
        JButton startGameButton = new JButton("Start game");
        startGameButton.addActionListener(e -> {
            newGameDialog.dispose();
            String player1Name = player1TextField.getText().replaceAll("\\s", "");
            player1Name = player1Name.equals("Computer") || player1Name.equals("computer") ? "oneName" : player1Name;
            String player2Name = player2TextField.getText().replaceAll("\\s", "");
            player2Name = !vsComputer && player2Name.equals("Computer") || player2Name.equals("computer") ? "twoName" : player2Name;
            startNewGame(player1Name, player2Name, vsComputer);

        });
        c.gridx = 0;
        c.gridy = 0;
        newGameDialog.add(player2Label, c);
        c.gridx = 1;
        c.gridy = 0;
        newGameDialog.add(player2TextField, c);
        c.gridx = 0;
        c.gridy = 1;
        newGameDialog.add(player1Label, c);
        c.gridx = 1;
        c.gridy = 1;
        newGameDialog.add(player1TextField, c);
        c.gridx = 1;
        c.gridy = 2;
        newGameDialog.add(startGameButton, c);
        newGameDialog.pack();
        newGameDialog.setLocationRelativeTo(parentFrame);
        newGameDialog.setVisible(true);
    }

    public static void changeCurrentPanel(JPanel newPanel) {
        window.remove(currentPanel);
        window.add(newPanel, BorderLayout.CENTER);
        currentPanel = newPanel;
        window.revalidate();
        window.repaint();
    }

    public static void changeRightPanel(JPanel newPanel) {
        window.remove(currentRightPanel);
        window.add(newPanel, BorderLayout.EAST);
        currentRightPanel = newPanel;
        window.pack();
        window.repaint();
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update(){
        //TODO
    }



    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public GameControllerPanel getControlPanel() {
        return controlPanel;
    }

    public static JFrame getWindow() {
        return window;
    }

    public static JPanel getCurrentPanel() {
        return currentPanel;
    }

    public static JPanel getCurrentRightPanel() {
        return currentRightPanel;
    }

    public GameModel getGame() {
        return game;
    }

    public Board getBoard() {
        return board;
    }
}
