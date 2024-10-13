package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    // Unique ID for each connection
    private static int uniqueID;
    // ArrayList to keep track of the list of clients
    private ArrayList<ClientThread> clients;
    // Display time stamp using simple date format
    private SimpleDateFormat dateFormat;
    // Port number to listen for connection
    private int portNumber;
    // Check if the chat is active and the server is running
    private boolean chatActive;

    // Constructor that takes in the port number as a parameter to listen for a
    // connection
    public Server(int portNumber) {
        this.portNumber = portNumber;
        dateFormat = new SimpleDateFormat();
        clients = new ArrayList<ClientThread>();
    }

    public static void main(String[] args) {
        int portNumber = 1500;
        switch(args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage: java Server [portNumber]");
                    return;
                }
            case 0:
                break;
            default:
                System.out.println("Usage: java Server [portNumber]");
        }
        Server server = new Server(portNumber);
        server.startServer();
    }

    public void startServer() {
        chatActive = true;

        // Create a socket server and wait for connection requests
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            // Loop as long as the chat server is active
            while (chatActive) {
                System.out.println("Server waiting for clients on port number " + portNumber + ".");
                // Accept connection request from client
                Socket socket = serverSocket.accept();

                // Break if server is stopped
                if (!chatActive)
                    break;
                // Create a thread for the client if it is connected
                ClientThread client = new ClientThread(socket);
                // Add client to array list of clients
                clients.add(client);
                // Start client thread
                client.start();
            }
            // Try closing the server
            try {
                serverSocket.close();
                int numberOfClients = clients.size();
                for (int i = 0; i < numberOfClients; i++) {
                    ClientThread client = clients.get(i);
                    // Close data streams and socket
                    try {
                        client.inputStream.close();
                        client.outputStream.close();
                        client.socket.close();
                    } catch (IOException e) {
                        System.out.println("Error in closing data streams and socket: " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error in closing the server and clients: " + e);
            }
        } catch (IOException e) {
            System.out.println("Error on new server socket: " + e);
        }
    }

    // Stop the server
    protected void stopServer() {
        chatActive = false;
        try {
            new Socket("localhost", portNumber);
        } catch (Exception e) { }
    }

    // Public chat: broadcast message to all clients
    private synchronized boolean broadcastMessage(String message) {
        // Add timestamp to the message
        String timestamp = dateFormat.format(new Date());

        // Extract first word from message to check if it is a private message
        // @Username -> private message
        String[] privateMessage = message.split(" ", 3);

        boolean isPrivate = false;
        if(privateMessage[1].charAt(0) == '@') isPrivate = true;

        if(isPrivate == true) {
            String receivedUsername = privateMessage[1].substring(1, privateMessage[1].length());
            message = privateMessage[0] + " " + privateMessage[2];
            String messageToSend = timestamp + " " + message;
            boolean foundUser = false;
            // Check if the username is found among active clients
            int numberOfClients = clients.size();
            for (int i = numberOfClients; --i >= 0;) {
                ClientThread client = clients.get(i);
                String clientUsername = client.getUsername();
                if(receivedUsername.equals(clientUsername)) {
                    if(!client.sendMessage(messageToSend)) {
                        clients.remove(i);
                        System.out.println("Client disconnected. " + client.username + " has been removed.");
                    }
                    foundUser = true;
                    break;
                }
            }
            if(foundUser != true) return false;
        }
        else {
            String messageToSend = timestamp + " " + message;
            System.out.println(messageToSend);
            int numberOfClients = clients.size();
            for (int i = numberOfClients; --i >= 0;) {
                ClientThread client = clients.get(i);
                if(!client.sendMessage(messageToSend)) {
                    clients.remove(i);
                    System.out.println("Client disconnected. " + client.username + " has been removed.");
                }
            }
        }
        return true;
    }

    synchronized void removeClient(int id) {
        String removedClient = "";
        int numberOfClients = clients.size();
        for (int i = 0; i < numberOfClients; i++) {
            ClientThread client = clients.get(i);
            // Remove client if found
            if(client.clientID == id) {
                removedClient = client.getUsername();
                clients.remove(i);
                break;
            }
        }
        broadcastMessage("***" + removedClient + " has left the chat room***");
    }

    class ClientThread extends Thread {
        // Socket to get messages from client
        Socket socket;
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        // Unique client id
        int clientID;
        // Client's username
        String username;
        // Chat message object
        ChatMessage chatMessage;
        // Timestamp
        String timestamp;

        public ClientThread(Socket socket) {
            clientID = uniqueID++;
            this.socket = socket;
            // Create data streams
            System.out.println("Thread is trying to create input/output streams.");
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                username = (String) inputStream.readObject();
                broadcastMessage("***" + username + " has joined the chatroom***");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in creating new input/output streams.");
            }
            timestamp = new Date().toString() + "\n";
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            boolean chatActive = true;
            while(chatActive) {
                try {
                    chatMessage = (ChatMessage) inputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error in reading streams: " + e);
                }
                String message = chatMessage.getMessage();
                switch(chatMessage.getType()) {
                    case ChatMessage.MESSAGE:
                        boolean userExists = broadcastMessage(username + ": " + message);
                        if(userExists == false) {
                            String msg = "***Sorry, no such user exists***";
                            sendMessage(msg);
                        }
                        break;
                    case ChatMessage.PARTICIPANTS:
                        sendMessage("List of users connected to the chat server at " + dateFormat.format(new Date()) + ":\n");
                        int numberOfClients = clients.size();
                        for (int i = 0; i < numberOfClients; i++) {
                            ClientThread client = clients.get(i);
                            sendMessage((i + 1) + ". " + client.username);
                        }
                        break;
                    case ChatMessage.LOGOUT:
                        System.out.println(username + " has logged out of the chat room.");
                        chatActive = false;
                        break;
                }
            }
            removeClient(clientID);
            disconnectChat();
        }

        // Close chat server
        private void disconnectChat() {
            try {
                if(outputStream != null) outputStream.close();
            } catch (Exception e) { }
            try {
                if(inputStream != null) inputStream.close();
            } catch (Exception e) { }
            try {
                if(socket != null) socket.close();
            } catch (Exception e) { }
        }

        private boolean sendMessage(String message) {
            // Send message if the client is still connected
            if(!socket.isConnected()) {
                disconnectChat();
                return false;
            }
            try {
                outputStream.writeObject(message);
            } catch (Exception e) {
                System.out.println("Error sending message to " + username);
            }
            return true;
        }
    }
}
