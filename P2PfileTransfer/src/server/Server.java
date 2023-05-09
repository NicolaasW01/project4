package server;

import shared.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int SERVER_PORT = 12345;
    private static Map<String, ClientHandler> connectedClients = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Message message = (Message) ois.readObject();
                String messageType = message.getMessageType();
                String clientId = message.getSender();

                if (messageType.equals("CONNECT")) {
                    if (connectedClients.containsKey(clientId)) {
                        oos.writeObject(new Message("ERROR", "Nickname already in use.", "SERVER", clientId));
                    } else {
                        ClientHandler clientHandler = new ClientHandler(socket, ois, oos, clientId);
                        connectedClients.put(clientId, clientHandler);
                        oos.writeObject(new Message("SUCCESS", "Connected to the server.", "SERVER", clientId));
                        new Thread(clientHandler).start();
                        System.out.println(clientId + " connected to the server!");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void removeClient(String clientId) {
        connectedClients.remove(clientId);
        System.out.println("Client '" + clientId + "' has left the server.");
    }
}
