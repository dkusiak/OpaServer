package opa;

import opa.rmi_interfaces.ILoginAndRegister;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginAndRegisterImpl extends UnicastRemoteObject implements ILoginAndRegister {
    protected LoginAndRegisterImpl() throws RemoteException {
    }

    @Override
    public boolean login(String username, String password) {
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

    @Override
    public boolean register(String username, String password) {
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
}
