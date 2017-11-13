package opa;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {


    public static Map<Integer, ConnectedUser> connectedUserList;
    private int currentUserId;

    public Server() {
        this.connectedUserList = new HashMap<>();
        this.currentUserId = 0;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Parameters.getPort());
            System.out.println("SERVER RUNNING on port: " + Parameters.getPort());

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());

                String[] userData = fromClient.readLine().split(":");
                if (userData[0].equals("LOGIN")) {
                    if (verifyUser(userData[1], userData[2])) {
                        toClient.writeBytes("OK\n");
                        connectedUserList.put(currentUserId, new ConnectedUser(currentUserId, socket, toClient, fromClient));
                        currentUserId++;
                    } else {
                        toClient.writeBytes("ERROR\n");
                    }
                }
                if (userData[0].equals("REGISTER")) {
                    if (addNewUser(userData[1], userData[2])) {
                        System.out.println("OK");
                        toClient.writeBytes("OK\n");
                    } else {
                        toClient.writeBytes("ERROR\n");
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean addNewUser(String username, String password) {

        try {
            File file = new File("users/" + username + "//password.txt");
            file.getParentFile().mkdir();
            file.createNewFile();
            DataOutputStream toFile = new DataOutputStream(new FileOutputStream(file));
            toFile.writeBytes(password);
        } catch (IOException e) {
            System.out.println("error");
        }

        return true;
    }


    private static boolean verifyUser(String username, String password) throws IOException {

        if (new File("users/" + username).exists()) {
            if (new File("users/" + username + "//password.txt").exists()) {

                try (BufferedReader reader = new BufferedReader(new FileReader(new File("users/" + username + "//password.txt")))) {
                    if (reader.readLine().trim().equals(password)) {
                        return true;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }
}
