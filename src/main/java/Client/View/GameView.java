package Client.View;

import Client.Controller.Loader;
import Client.Model.*;
import Server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * GameView class is responsible for managing the game window and handling game-related actions.
 * It includes methods for initializing the game, starting a new game, continuing a saved game,
 * reviewing a finished game, and loading saved game or replay files.
 */
public class GameView extends JPanel{
    private JFrame window;
    private JPanel currentPanel;
    private JPanel currentRightPanel;
    private final GameModel game;
    private BoardPanel boardPanel;
    private GameControllerPanel controlPanel;

    /**
     * Constructor for GameView class.
     *
     * @param game The game model.
     */
    public GameView(GameModel game) {
        this.game = game;
    }

    /**
     * Initializes the game window and sets up the welcome panel.
     */
    public void init() {
        window = new JFrame("Arimaa Game");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    /**
     * Starts a new game with specified player names and game mode.
     *
     * @param player1name Name of player 1.
     * @param player2name Name of player 2.
     * @param vsComputer  Whether the game is against a computer.
     */
    private void startNewGame(String player1name, String player2name, boolean vsComputer){
        Player player1 = new Player(1, player1name, false);
        Player player2 = new Player(2, player2name, vsComputer);
        game.setPlayers(player1, player2);
        game.setCurrentPlayer(player1,player2);
        game.getBoard().populateBoardFrom2DString(GameModel.DEFAULT_BOARD, player1, player2);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        controlPanel = new GameControllerPanel(game, boardPanel, vsComputer, 0, this);
        game.setGameListener(controlPanel);
        currentRightPanel = controlPanel;
        changeRightPanel(currentRightPanel);
    }

    /**
     * Initializes and starts an online game with the specified players and client.
     * This method sets up the game state, board configuration, and UI components
     * for an online game session.
     *
     * @param player1name The name of the first player.
     * @param player2name The name of the second player.
     * @param playerId The ID of the first player.
     * @param playerEnemyId The ID of the second player (the enemy player).
     * @param client The client object used for online communication.
     */
    public void startOnlineGame(String player1name, String player2name, int playerId, int playerEnemyId, Client client) {
        Player player1 = new Player(playerId, player1name, false);
        Player player2 = new Player(playerEnemyId, player2name, false);
        game.setPlayers(player1, player2);
        game.setCurrentPlayer(player1,player2);
        if (playerId == 1)
            game.getBoard().populateBoardFrom2DString(GameModel.DEFAULT_BOARD, player1, player2);
        else
            game.getBoard().populateBoardFrom2DString(GameModel.DEFAULT_BOARD_ROTATED, player2, player1);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        controlPanel = new GameControllerPanel(game, boardPanel, false, playerId, this);
        game.setClient(client);
        controlPanel.setClient(client);
        boardPanel.setClient(client);
        game.setGameListener(controlPanel);
        currentRightPanel = controlPanel;
        changeRightPanel(currentRightPanel);
    }

    /**
     * Updates the game view based on the provided GameState object.
     * This method updates the board, control panel, and various game components
     * to reflect the current state of the game. It handles both ongoing and finished games.
     *
     * @param gameState The current state of the game.
     */
    public void updateView(GameState gameState){
        if (gameState.phase == game.getPhase() && !gameState.isGameFinished) {
            game.getBoard().populateBoardFrom2DStringRotated(gameState.boardState, game.getPlayer1(), game.getPlayer2());
            controlPanel.boardSnap();
            boardPanel.setGame(game);
            changeCurrentPanel(boardPanel);
            game.setGameListener(controlPanel);
        }
        else {
            game.updateGameState(gameState);
            game.getBoard().populateBoardFrom2DStringRotated(gameState.boardState, game.getPlayer1(), game.getPlayer2());
            boardPanel.setGame(game);
            changeCurrentPanel(boardPanel);
            game.setGameListener(controlPanel);
            if (game.isGameFinished()) {
                controlPanel.onGameEnded(game.getWinner());
            }
            if (game.getPhase() > 1 && !game.isGameFinished())
                controlPanel.setTurnFormatting();
        }
    }

    /**
     * Continues a game from a saved state.
     *
     * @param gameState The saved game state.
     */
    private void continueGame(ArrayList<GameState> gameState) {
        Player player1 = new Player(1, gameState.get(gameState.size()-1).player1.getPlayerName(), false);
        Player player2 = new Player(2, gameState.get(gameState.size()-1).player2.getPlayerName(), gameState.get(0).vsComputer);
        game.setPlayers(player1, player2);
        game.setCurrentPlayer(gameState.get(gameState.size()-1).currentPlayer, gameState.get(gameState.size()-1).enemyPlayer);
        game.getBoard().populateBoardFrom2DString(gameState.get(gameState.size()-1).boardState, player1, player2);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        controlPanel = new GameControllerPanel(game, boardPanel, gameState.get(0).vsComputer, 0,this);
        game.setGameListener(controlPanel);
        currentRightPanel = controlPanel;
        changeRightPanel(currentRightPanel);
        controlPanel.startTimerWithRemainingTime(gameState.get(gameState.size()-1).remainingTime);
        controlPanel.loadSnapInfo(gameState);
    }

    /**
     * Reviews a finished game from a saved replay state.
     *
     * @param gameState The saved replay state.
     */
    private void reviewGame(ArrayList<GameState> gameState) {
        Player player1 = new Player(1, gameState.get(gameState.size()-1).player1.getPlayerName(), false);
        Player player2 = new Player(2, gameState.get(gameState.size()-1).player2.getPlayerName(), false);
        game.setPlayers(player1, player2);
        game.getBoard().populateBoardFrom2DString(gameState.get(0).boardState, player1, player2);
        boardPanel = new BoardPanel(game);
        boardPanel.setGame(game);
        changeCurrentPanel(boardPanel);
        ReplayControllerPanel replayPanel = new ReplayControllerPanel(game, this);
        currentRightPanel = replayPanel;
        changeRightPanel(currentRightPanel);
        replayPanel.loadSnapInfo(gameState);
    }

    /**
     * Loads a saved game state from a file.
     *
     * @param vsComputer Whether the game is against a computer.
     */
    protected void loadSave(boolean vsComputer) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            ArrayList<GameState> gameState = Loader.loadGame(fileToLoad);

            //checks for equal game mode of a save file and selected menu and if game is still going on
            if (gameState != null && !gameState.get(gameState.size()-1).isGameFinished && vsComputer == gameState.get(0).vsComputer) {
                game.loadState(gameState);
                continueGame(gameState);
            }else if(gameState != null && vsComputer != gameState.get(0).vsComputer && !vsComputer) {
                JOptionPane.showMessageDialog(
                        null,
                        "The save file is meant for single player mode.",
                        "Load Game",
                        JOptionPane.WARNING_MESSAGE
                );
            }else if(gameState != null && vsComputer != gameState.get(0).vsComputer && vsComputer){
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

    /**
     * Loads a game replay from a file.
     */
    protected void loadReplay() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Replay");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            ArrayList<GameState> gameState = Loader.loadGame(fileToLoad);

            //checks if game was finished to view replay
            if (gameState != null && gameState.get(gameState.size()-1).isGameFinished) {
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

    /**
     * Displays a window for entering players names and starting a new game.
     *
     * @param parentFrame The parent frame for the dialog.
     * @param vsComputer  Whether the game is against a computer.
     */
    protected void showNewGameDialog(JFrame parentFrame, boolean vsComputer) {
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

    /**
     * Changes the current main panel to a new panel.
     *
     * @param newPanel The new panel to be displayed.
     */
    protected void changeCurrentPanel(JPanel newPanel) {
        window.remove(currentPanel);
        window.add(newPanel, BorderLayout.CENTER);
        currentPanel = newPanel;
        window.revalidate();
        window.repaint();
    }

    /**
     * Changes the current right panel to a new panel.
     *
     * @param newPanel The new panel to be displayed.
     */
    protected void changeRightPanel(JPanel newPanel) {
        window.remove(currentRightPanel);
        window.add(newPanel, BorderLayout.EAST);
        currentRightPanel = newPanel;
        window.pack();
        window.repaint();
    }


    /**
     * Closes the game window.
     */
    protected void closeWindow(){
        window.dispose();
    }

    public JFrame getWindow() {
        return window;
    }
    public GameModel getGame() {
        return game;
    }
}