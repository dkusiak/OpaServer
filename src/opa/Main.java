package opa;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Parameters.loadConnectionParams();
            new Thread(new Server()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
