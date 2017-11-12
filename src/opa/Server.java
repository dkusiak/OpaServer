package opa;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static DataOutputStream toClient;
    static BufferedReader fromClient;

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(7777);

                    while(true){
                        Socket socket = serverSocket.accept();
                        fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        toClient = new DataOutputStream(socket.getOutputStream());
                        String[] loginData = fromClient.readLine().split(":");

                        if(loginData[0].equals("LOGIN"))
                            verifyUser(loginData[1], loginData[2]);
                    }

                } catch (IOException e) {e.printStackTrace();}
            }
        });
        thread.start();
    }

    private static void verifyUser(String username, String password) throws IOException {

        if(new File(username).exists()){
            if(new File(username + "//password.txt").exists()){

                try(BufferedReader reader = new BufferedReader(new FileReader(new File(username + "//password.txt")))){
                    if(reader.readLine().trim().equals(password)) {
                        toClient.writeBytes("OK");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();}

            }
        }
        else{
            toClient.writeBytes("ERROR");
        }
    }

}
