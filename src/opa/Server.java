package opa;

import java.io.*;
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
                    System.out.println("SERVER RUNNING on port: " + serverPort);
                    Socket socket = serverSocket.accept();
                    fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toClient = new DataOutputStream(socket.getOutputStream());

                    while(true){

                        String[] userData = fromClient.readLine().split(":");
                        if(userData[0].equals("LOGIN")) {
                            if (verifyUser(userData[1], userData[2])) {
                                toClient.writeBytes("OK\n");
                            } else {
                                toClient.writeBytes("ERROR\n");
                            }
                        }
                        if(userData[0].equals("REGISTER")){
                            if(addNewUser(userData[1],userData[2])){
                                System.out.println("OK");
                                toClient.writeBytes("OK\n");
                            }
                            else {
                                toClient.writeBytes("ERROR\n");
                            }
                        }

                    }

                } catch (IOException e) {e.printStackTrace();}
            }
        });
        thread.start();
    }

    private static boolean addNewUser(String username, String password) {

        try {
            File file = new File(username + "//password.txt");
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

        if(new File(username).exists()){
            if(new File(username + "//password.txt").exists()){

                try(BufferedReader reader = new BufferedReader(new FileReader(new File(username + "//password.txt")))){
                    if(reader.readLine().trim().equals(password)) {
                        return true;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();}

            }
        }
        return false;
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
