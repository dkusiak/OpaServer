package opa.rmi_interfaces;

import com.healthmarketscience.rmiio.RemoteInputStream;
import javafx.collections.ObservableList;
import opa.archived_file.ArchivedFile;
import opa.archived_file.ArchivedFileVersion;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IFileManager extends Remote {
    boolean uploadFile(String username, String filename, String localPath, RemoteInputStream remoteInputStream) throws IOException, RemoteException;
    byte[] downloadFile(ArchivedFileVersion archivedFileVersion) throws RemoteException;
    ArrayList<ArchivedFile> getArchivedFiles(String username) throws RemoteException;
}
