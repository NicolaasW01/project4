package client;

import shared.Message;
import shared.FileSearchResult;
import shared.EncryptionUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
       try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your nickname: ");
            String nickname = scanner.nextLine();

            oos.writeObject(new Message("CONNECT", null, nickname, null));

            Message response = (Message) ois.readObject();
            System.out.println(response.getContent());

            new Thread(() -> {
                try {
                    while (true) {
                        Message message = (Message) ois.readObject();
                        if (message.getMessageType().equals("SEARCH_RESULTS")) {
                            List<FileSearchResult> searchResults = message.getSearchResults();
                            // Handle search results here

                        } else if (message.getMessageType().equals("DOWNLOAD_REQUEST")) {
                            //Hanlde downloads requeast
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                System.out.println("Enter 'search' to search for files, or 'exit' to quit:");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("search")) {
                    System.out.print("Enter your search query: ");
                    String query = scanner.nextLine();
                    oos.writeObject(new Message("SEARCH", query, nickname, null));
                } else if (command.equalsIgnoreCase("exit")) {
                    oos.writeObject(new Message("DISCONNECT", null, nickname, null));
                    break;
                } else {
                    System.out.println("Invalid command.");
                }
            }

            scanner.close();
            ois.close();
            oos.close();
            socket.close();
        
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
