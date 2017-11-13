package opa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parameters {
    private static int serverPort;
    private static final String RESOURCES_PATH = "..\\OpaServer\\src\\resources\\";
    private static final String CONNECTION_PARAMS = "connection_params.txt";

    public static void loadConnectionParams() throws IOException {
        FileReader fileReader = new FileReader(RESOURCES_PATH + CONNECTION_PARAMS);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        setPort(Integer.valueOf(bufferedReader.readLine().split("=")[1]));
        fileReader.close();
    }


    public static void setPort(Integer port) { serverPort = port; }
    public static int getPort() { return serverPort; }
}
