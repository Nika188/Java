package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // Input stream to read from the socket
    private ObjectInputStream inputStream;
    // Output stream to write on the socket
    private ObjectOutputStream outputStream;
    // Socket object
    private Socket socket;

    private String server;
    private String username;
    private int portNumber;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Client(String server, int portNumber, String username) {
        this.server = server;
        this.username = username;
        this.portNumber = portNumber;
    }

    public boolean startClient() {
        try {
            socket = new Socket(server, portNumber);
        } catch (Exception e) {
            System.out.println("Error connecting to the server: " + e);
            return false;
        }
        System.out.println("Connection accepted " + socket.getInetAddress() + ": " + socket.getPort());

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error creating new input/output streams: " + e);
            return false;
        }
        // Create the thread to listen from the server
        new ListenForServerMessage().start();
        try {
            outputStream.writeObject(username);
        } catch (IOException e) {
            System.out.println("Error during login: " + e);
            disconnectChat();
            return false;
        }
        return true;
    }

    // Send message to the server
    void sendMessage(ChatMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error in writing to the server: " + e);
        }
    }

    // Close chat server
    private void disconnectChat() {
        try {
            if (outputStream != null)
                outputStream.close();
        } catch (Exception e) {}
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (Exception e) {}
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {}
    }

    class ListenForServerMessage extends Thread {
        @Override
        public void run() {
            while(true) {
                try {
                    String message = (String) inputStream.readObject();
                    System.out.println(message);
                    System.out.print(">> ");
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error in connection: " + e);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        // Default configuration values
        int portNumber = 1500;
        String serverAddress = "localhost";
        String username = "Anonymous";
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter your username: ");
        username = scan.nextLine();

        // different case according to the length of the arguments.
        switch(args.length) {
            // javac Client [username] [portNumber] [serverAddress]
            case 3:
                serverAddress = args[2];
                // javac Client [username] [portNumber]
            case 2:
                try {
                    portNumber = Integer.parseInt(args[1]);
                }
                catch(Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage: java Client [username] [portNumber] [serverAddress]");
                    return;
                }
                // javac Client [username]
            case 1:
                username = args[0];
                // java Client
            case 0:
                break;
            // Invalid arguments
            default:
                System.out.println("Usage: java Client [username] [portNumber] [serverAddress]");
                return;
        }

        // Create client object
        Client client = new Client(serverAddress, portNumber, username);

        // Try to connect to the server
        if(!client.startClient()) return;

        System.out.println("\nHello.! Welcome to the chatroom.");
        System.out.println("Instructions:");
        System.out.println("1. Simply type the message to send broadcast to all active clients");
        System.out.println("2. Type '@username<space>yourmessage' without quotes to send message to desired client");
        System.out.println("3. Type 'PARTICIPANTS' without quotes to see list of active clients");
        System.out.println("4. Type 'LOGOUT' without quotes to logoff from server");

        // Get user input
        while(true) {
            System.out.print(">> ");
            // Input message from user
            String messageToSend = scan.nextLine();
            // Logout of the chat room
            if(messageToSend.equalsIgnoreCase("LOGOUT")) {
                client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                break;
            }
            // View participants in the chat room
            else if(messageToSend.equalsIgnoreCase("PARTICIPANTS")) {
                client.sendMessage(new ChatMessage(ChatMessage.PARTICIPANTS, ""));
            }
            // Send text message
            else {
                client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, messageToSend));
            }
        }
        scan.close();
        client.disconnectChat();
    }
}

class ChatMessage implements Serializable {

    // The different types of message sent by the Client
    // PARTICIPANTS - receive a list of the connected clients
    // MESSAGE - text message to send as private/public message
    // LOGOUT - disconnect from server
    static final int PARTICIPANTS = 0, MESSAGE = 1, LOGOUT = 2;
    private int type;
    private String message;

    // Constructor
    ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    int getType() {
        return type;
    }

    String getMessage() {
        return message;
    }
}