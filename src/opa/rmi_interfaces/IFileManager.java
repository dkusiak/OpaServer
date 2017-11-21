package opa.rmi_interfaces;

import com.healthmarketscience.rmiio.RemoteInputStream;
import javafx.collections.ObservableList;
import opa.archived_file.ArchivedFile;
import opa.archived_file.ArchivedFileVersion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IFileManager extends Remote {
    boolean uploadFile(String username, String filename, String localPath, RemoteInputStream remoteInputStream, String modified) throws IOException;
    RemoteInputStream downloadFile(ArchivedFileVersion archivedFileVersion) throws IOException;
    ArrayList<ArchivedFile> getArchivedFiles(String username) throws RemoteException;
    String checkLastModified(String username, String filename) throws RemoteException;
    long checkFileSize(String filePath) throws RemoteException;
}
