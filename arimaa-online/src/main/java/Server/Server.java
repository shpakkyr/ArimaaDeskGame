package Server;

import Client.Model.GameState;
import Client.Model.Player;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 9999;
    private static final ExecutorService pool = Executors.newFixedThreadPool(2);
    private static Socket client1 = null;
    private static Socket client2 = null;
    private static String playerName1 = null;
    private static String playerName2 = null;
    public static final CountDownLatch latch = new CountDownLatch(2);
    private boolean bothConnected = false;

    public static String enemyPlayer(String player) {
        return playerName2;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        server.startServer(args);
    }

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

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final Socket otherSocket;
        private ObjectOutputStream outObject;
        private ObjectInputStream inObject;
        private String player;
        private boolean flag = false;
        private boolean bothConnected;

        public ClientHandler(Socket clientSocket, Socket otherSocket, String player, ObjectInputStream ois, ObjectOutputStream oos, boolean bothConnected) {
            this.clientSocket = clientSocket;
            this.otherSocket = otherSocket;
            this.player = player;
            inObject = ois;
            outObject = oos;
            this.bothConnected = bothConnected;
        }

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
//                            System.out.println("Received GameState from client: " + player);
//                            System.out.println("GameState is: " + gameState);
                            String prepareReceiveObject = "prepareReceiveObject";
                            outObject.writeObject(prepareReceiveObject);
                            outObject.writeObject(gameState);
                            outObject.flush();
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
                        inObject.close();
                        outObject.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
