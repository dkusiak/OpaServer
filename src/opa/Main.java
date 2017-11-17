package opa;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            Parameters.loadConnectionParams();
            Registry registry = LocateRegistry.createRegistry(Parameters.getPort());
            LoginAndRegisterImpl loginAndRegister = new LoginAndRegisterImpl();
            FileManagerImpl fileManager = new FileManagerImpl();
            registry.bind("loginAndRegister", loginAndRegister);
            registry.bind("fileManager", fileManager);
            //new Thread(new Server()).start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
