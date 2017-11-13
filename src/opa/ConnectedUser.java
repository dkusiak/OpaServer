package opa;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedUser implements Runnable {

    private int id;
    private Socket socket;
    private DataOutputStream toClient;
    private BufferedReader fromClient;

    public ConnectedUser(int id, Socket socket, DataOutputStream toClient, BufferedReader fromClient) {
        this.id = id;
        this.socket = socket;
        this.toClient = toClient;
        this.fromClient = fromClient;

        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean isConnected = true;
        while (isConnected) {
            try {
                String userData = fromClient.readLine();
                //Todo logika polaczenia z konkretnym klientem
            } catch (IOException e) {
                isConnected = false;
                disconnectClient();
                System.out.println("Rozłączono klienta o ID: " + id);
            }
        }
    }

    private void disconnectClient() {
        Server.connectedUserList.remove(id);
    }
}
