package Server;

import java.io.*;
import java.net.*;

public class ServerArimaa {
    public static void main(String[] args) {
        int portNumber = 3228;

            //vytváří nový objekt třídy ServerSocket a naslouchá na portNumber požadavkům na připojení od klientu
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is on port: " + portNumber);

            // First player connects

            // když se klient připojí, vytvoří se nový soketový objekt pro komunikaci s tímto klientem
            Socket player1Socket = serverSocket.accept();
            // vytváří výstupní proudy pro komunikaci s každým klientem
            PrintWriter player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
            //vytváří vstupní proudy pro komunikaci s každým klientem
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));

            // Second player connects
            Socket player2Socket = serverSocket.accept();
            PrintWriter player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));


        } catch (IOException e) {
            System.out.println("Error while starting server: " + e.getMessage());
        }
    }
}
