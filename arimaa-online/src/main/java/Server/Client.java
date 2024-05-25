package Server;

import Client.View.GameView;
import Client.Model.GameState;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.Buffer;

/**
 * The Client class manages the connection to the game server and handles communication
 * between the client and server, including sending and receiving game states.
 */
public class Client {
    private static final String SERVER_IP = "localhost"; // Use localhost" for the same PC
    private static final int SERVER_PORT = 9999;
    private static String playerName;
    private static String playerEnemy;
    private int playerId;
    private Socket socket;
    private ObjectOutputStream outObject;
    private ObjectInputStream inObject;
    private boolean flag = false;

    /**
     * The main method for starting the client application for online multiplayer.
     *
     * @param args Command-line arguments, where the first argument is the player's name.
     * @param view The GameView object for managing the game UI.
     */
    public static void main(String[] args, GameView view) throws IOException {
        Client client = new Client();
        client.startClient(args, view);
    }

    /**
     * Starts the client and establishes a connection to the server.
     * This method sets up input and output streams for communication and handles
     * the reception of game state updates and other messages from the server.
     *
     * @param args Command-line arguments, where the first argument is the player's name.
     * @param view The GameView object for managing the game UI.
     */
    public void startClient(String[] args, GameView view) throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            this.socket = socket;
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            outObject = new ObjectOutputStream(os);
            inObject = new ObjectInputStream(is);

            playerId = (int) inObject.readObject();
            System.out.println("My player id is " + playerId);

            if (args.length > 0) {
                playerName = args[0];
                outObject.writeObject(playerName);
            }

            System.out.println(playerName + " connected to the server");

            while (true) {
                Object obj = inObject.readObject();  // Read a single object from the stream
                if (obj instanceof String) { // Check if it is a String
                    String message = (String) obj;
                    if (message.equals("prepareReceiveObject")) {
                        Object potentialGameState = inObject.readObject();
                        if (potentialGameState instanceof GameState) {
                            GameState gameState = (GameState) potentialGameState;
                            view.updateView(gameState);
                        } else {
                            System.out.println("Expected a Player, but received something else.");
                        }
                    } else if (message.equals("Both players are connected.")) {
                        String getEnemy = "getEnemy";
                        outObject.writeObject(getEnemy);
                        playerEnemy = (String) inObject.readObject();
                        int playerEnemyId = playerId == 1 ? 2 : 1;
                        System.out.println(playerEnemy);
                        // Notify the player that the game can start
                        SwingUtilities.invokeLater(() -> {
                            view.startOnlineGame(playerName, playerEnemy, playerId, playerEnemyId, this);
                        });
                    } else if (message.equals("finishCommunicationWithServer")) {
                        System.out.println("I'm " + playerName + " finishing communication with server");
                        flag = true;
                        break;
                    }
                } else {
                    System.out.println("Received non-string object, handling not implemented.");
                }
            }


            // Sending messages to the server
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                inObject.close();
                outObject.close();
                socket.close();
            }
        }
    }

    /**
     * Sends the current game state to the enemy player via the server.
     *
     * @param gameState The current game state to be sent.
     * @throws IOException If an I/O error occurs while sending the game state.
     */
    public void sendObjectToEnemy(GameState gameState) throws IOException {
        outObject.writeObject("sendGameState");
        outObject.writeObject(gameState);
    }

    /**
     * Sends the message to the server.
     *
     * @param message The message that should be sent.
     * @throws IOException If an I/O error occurs while sending the game state.
     */
    public void sendMessageToServer(String message) throws IOException {
        outObject.writeObject(message);
    }
}