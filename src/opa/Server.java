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
    private static int serverPort;
    private static final String RESOURCES_PATH = "..\\server\\src\\opa\\";
    private static final String CONNECTION_PARAMS = "connection_params.txt";

    public static void main(String[] args) {

        try {
            loadConnectionParams();
        } catch (IOException e) { e.printStackTrace(); }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(serverPort);

                    while(true){
                        System.out.println("SERVER RUNNING on port: " + serverPort);
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

    private static void loadConnectionParams() throws IOException {
        FileReader fileReader = new FileReader(RESOURCES_PATH + CONNECTION_PARAMS);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        setPort(Integer.valueOf(bufferedReader.readLine().split("=")[1]));
        fileReader.close();
    }

    public static void setPort(Integer port) { serverPort = port; }
    public static int getPort() { return serverPort; }

}
