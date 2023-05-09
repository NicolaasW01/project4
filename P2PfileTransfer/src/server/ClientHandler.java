package server;

import shared.Message;
import shared.FileSearchResult;
import shared.EncryptionUtil;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String clientId;

    public ClientHandler(Socket socket, ObjectInputStream ois, ObjectOutputStream oos, String clientId) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) ois.readObject();
                String messageType = message.getMessageType();
                String targetClient = message.getRecipient();

                if (messageType.equals("SEARCH")) {
                    // Handle search requests here
                } else if (messageType.equals("DOWNLOAD_REQUEST")) {
                    ClientHandler targetClientHandler = Server.getClient(targetClient);
                    if (targetClientHandler != null) {
                        targetClientHandler.oos.writeObject(message);
                    } else {
                        oos.writeObject(new Message("ERROR", "Target client not found.", "SERVER", clientId));
                    }
                } else if (messageType.equals("DISCONNECT")) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                ois.close();
                oos.close();
                Server.removeClient(clientId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<FileSearchResult> performSearch(String query) {
        List<FileSearchResult> results = new ArrayList<>();
        // Add search logic here to search for files shared by clients

        return results;
    }
}

