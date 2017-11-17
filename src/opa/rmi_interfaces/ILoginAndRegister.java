package opa.rmi_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILoginAndRegister extends Remote {
    boolean login(String username, String password) throws RemoteException;;

    boolean register(String username, String password) throws RemoteException;;
}
