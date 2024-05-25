package Server;

import Client.Model.GameState;
import Client.Model.Player;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * The Server class manages connections to clients and handles communication between them.
 * It uses a thread pool to manage multiple client connections simultaneously.
 */
public class Server {
    private static final int PORT = 9999;
    private static final ExecutorService pool = Executors.newFixedThreadPool(2);
    private static Socket client1 = null;
    private static Socket client2 = null;
    private static String playerName1 = null;
    private static String playerName2 = null;
    public static final CountDownLatch latch = new CountDownLatch(2);
    private boolean bothConnected = false;

    /**
     * The main method for starting the server application.
     *
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If a class cannot be found during object deserialization.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        server.startServer(args);
    }

    /**
     * Starts the server and waits for clients to connect.
     * This method sets up input and output streams for communication and handles
     * the reception of game state updates and other messages from the clients.
     *
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If a class cannot be found during object deserialization.
     */
    public void startServer(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server is waiting for clients on port " + PORT);
        ObjectOutputStream outObject1 = null;
        ObjectOutputStream outObject2;
        ObjectInputStream inObject1 = null;
        ObjectInputStream inObject2;

        new Thread(() -> {
            try {
                latch.await();
                System.out.println("Both clients are connected.");
                bothConnected = true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        while (client1 == null || client2 == null) {
            Socket clientSocket = serverSocket.accept();
            System.out.print("Connected to client ");

            if (client1 == null) {
                client1 = clientSocket;
                OutputStream os = client1.getOutputStream();
                InputStream is = client1.getInputStream();
                outObject1 = new ObjectOutputStream(os);
                inObject1 = new ObjectInputStream(is);
                int id = 1;
                outObject1.writeObject(id);
                playerName1 = (String) inObject1.readObject();
                System.out.println(playerName1);
                latch.countDown();
            } else if (client2 == null) {
                client2 = clientSocket;
                OutputStream os = client2.getOutputStream();
                InputStream is = client2.getInputStream();
                outObject2 = new ObjectOutputStream(os);
                inObject2 = new ObjectInputStream(is);
                int id = 2;
                outObject2.writeObject(id);
                playerName2 = (String) inObject2.readObject();
                System.out.println(playerName2);
                latch.countDown();
                pool.execute(new ClientHandler(client1, client2, playerName1, inObject1, outObject2, bothConnected));
                pool.execute(new ClientHandler(client2, client1, playerName2, inObject2, outObject1, bothConnected));
            }
        }

        serverSocket.close();
    }

    /**
     * ClientHandler handles communication with a single client.
     */
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ObjectOutputStream outObject;
        private final ObjectInputStream inObject;
        private final String player;
        private boolean flag = false;
        private final boolean bothConnected;

        /**
         * Constructs a new ClientHandler for the specified client and other client.
         *
         * @param clientSocket The socket for the connected client.
         * @param otherSocket The socket for the other connected client.
         * @param player The name of the player associated with this client.
         * @param ois The input stream for reading objects from the client.
         * @param oos The output stream for writing objects to the client.
         * @param bothConnected Indicates whether both clients are connected.
         */
        public ClientHandler(Socket clientSocket, Socket otherSocket, String player, ObjectInputStream ois, ObjectOutputStream oos, boolean bothConnected) {
            this.clientSocket = clientSocket;
            this.player = player;
            inObject = ois;
            outObject = oos;
            this.bothConnected = bothConnected;
        }

        /**
         * Handles the communication with the connected client.
         * This method continuously listens for and processes incoming objects from the client's input stream.
         * It handles different types of messages, such as retrieving the enemy player's name, showing messages on the server side,
         * and sending game state updates to the other client.
         */
        @Override
        public void run() {
            try {
                if(bothConnected) {
                    String message = "Both players are connected.";
                    outObject.writeObject(message);
                }
                while (true) {
                    Object obj = inObject.readObject();  // Read a single object from the stream
                    if (obj instanceof String) { // Check if it is a String
                        String message = (String) obj;
                        if (message.equals("getEnemy")) {
                            outObject.writeObject(Objects.equals(player, playerName1) ? playerName1 : playerName2);
                        } else if (message.equals("showMessageOnServerSide")) {
                            String messageToShow = (String) inObject.readObject();
                            System.out.println(messageToShow);
                        } else if (message.equals("sendGameState")) {
                            GameState gameState = (GameState) inObject.readObject();
                            String prepareReceiveObject = "prepareReceiveObject";
                            outObject.writeObject(prepareReceiveObject);
                            outObject.writeObject(gameState);
                            outObject.flush();
                        } else if (message.equals("finishCommunicationWithServer")) {
                            outObject.writeObject("finishCommunicationWithServer");
                            outObject.flush();
                            flag = true;
                            return;
                        }
                    } else {
                        System.out.println("Received non-string object, handling not implemented.");
                    }
                }

            } catch (IOException e) {
                System.out.println("Error handling client #" + clientSocket.getPort() + ": " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (flag) {
                        System.out.println("Server is stopping!");
                        outObject.close();
                        inObject.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
