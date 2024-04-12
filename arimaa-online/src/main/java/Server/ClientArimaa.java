package Server;

import java.io.*;
import java.net.*;

public class ClientArimaa {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int portNumber = 3228;

        try (
                Socket socket = new Socket(serverAddress, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverAddress);
        } catch (IOException e) {
            System.err.println("Error in communication with server: " + e.getMessage());
        }
    }
}
