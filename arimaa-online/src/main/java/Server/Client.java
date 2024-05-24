package Server;

import Client.View.GameView;
import Client.Model.GameState;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.Buffer;

public class Client {
    private static final String SERVER_IP = "localhost"; // Use localhost" for the same PC
    private static final int SERVER_PORT = 9999;
    private static String playerName;
    private static String playerEnemy;
    private int playerId;
    private Socket socket;
    private ObjectOutputStream outObject;
    private ObjectInputStream inObject;

    public static void main(String[] args, GameView view) {
        Client client = new Client();
        client.startClient(args, view);
    }

    public void startClient(String[] args, GameView view) {
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
        }
    }

    public void sendMessageToServer(String message) throws IOException {
        String messageC = "showMessageOnServerSide";
        outObject.writeObject("showMessageOnServerSide");
        outObject.writeObject(message);
    }

    public void sendObjectToEnemy(GameState gameState) throws IOException {
        outObject.writeObject("sendGameState");
        outObject.writeObject(gameState);
    }
}