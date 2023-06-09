package client;

import shared.Message;
import shared.FileSearchResult;
import shared.EncryptionUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Scanner scanner;
    private static volatile boolean handlingDownloadRequest = false;


    public static void main(String[] args) {
       try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);
            String nickname;

            while (true) {
                System.out.print("Enter your nickname: ");
                nickname = scanner.nextLine();
                oos.writeObject(new Message("CONNECT", null, nickname, null));

                Message response = (Message) ois.readObject();
                System.out.println(response.getContent());

                if (response.getMessageType().equals("SUCCESS")) {
                    break;
                }
            }

            final String finalNickname = nickname;

            // Start a separate thread to handle incoming messages from the server
            new Thread(() -> {
                try {
                    while (true) {
                        Message message = (Message) ois.readObject();
                        String messageType = message.getMessageType();

                        if (messageType.equals("SEARCH_RESULTS")) {
                            List<FileSearchResult> searchResults = message.getSearchResults();
                            // Handle search results here

                        } else if (messageType.equals("DOWNLOAD_REQUEST")) {
                            //set handle request boolean to true
                            handlingDownloadRequest = true;
                            //Hanlde downloads requests here
                            String fileName = message.getContent();
                            String sender = message.getSender();
                            String downloadKey = message.getDownloadKey();

                            System.out.println(sender + " wants to download the file: " + fileName);
                            System.out.print("Do you want to accept the request? (y/n): ");
                            String response = readLine().trim().toLowerCase();

                            if (response.equals("y")) {
                                // User accepted the request
                                // Establish a direct connection and transfer the file
                                try (ServerSocket serverSocket = new ServerSocket(1030)) {
                                    
                                    int port = serverSocket.getLocalPort();
                                    oos.writeObject(new Message("DOWNLOAD_INFO", fileName, finalNickname, sender, port, null));
                                    
                                    try (Socket transferSocket = serverSocket.accept()) {
                                        // Transfer the file to the requesting client
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if(response.equals("n")) {
                                System.out.println("Request declined.");
                            }

                        } else if (messageType.equals("DOWNLOAD_INFO")) {
                            // Handle download info here
                            int port = message.getPort();
                            String targetClientAddress = message.getRecipientAddress();
                        
                            try (Socket transferSocket = new Socket(targetClientAddress, port)) {
                                // Receive the file from the target client
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                System.out.println("Enter 'search' to search for files, 'download' to request a file download, or 'exit' to quit:");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("search")) {

                    System.out.print("Enter your search query: ");
                    String query = scanner.nextLine();
                    oos.writeObject(new Message("SEARCH", query, nickname, null));
                    
                } else if (command.equalsIgnoreCase("download")) {
                
                    System.out.print("Enter the target client's nickname: ");
                    String targetClient = scanner.nextLine();
                    System.out.print("Enter the file name to download: ");
                    String fileName = scanner.nextLine();

                    SecureRandom random = new SecureRandom();
                    int downloadKey = random.nextInt();
                    String strDownloadKey = Integer.toString(downloadKey);
                    oos.writeObject(new Message("DOWNLOAD_REQUEST", fileName, nickname, targetClient, strDownloadKey));
                   
                } else if (command.equalsIgnoreCase("exit")) {
                    oos.writeObject(new Message("DISCONNECT", null, nickname, null));
                    break;

                } else if (handlingDownloadRequest) {
                    // Handling user response for download request
                    if (command.equalsIgnoreCase("y")) {
                        // ...
                        System.out.println("Peer-to-peer connection succesfully created...");
                    } else if (command.equalsIgnoreCase("n")) {
                        System.out.println("Request declined.");
                    }
                    handlingDownloadRequest = false;
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

    static {
        scanner = new Scanner(System.in);
    }
    

    private static synchronized String readLine() {
        return scanner.nextLine();
    }
    
}
