package opa;

import com.healthmarketscience.rmiio.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Arc;
import opa.archived_file.ArchivedFile;
import opa.archived_file.ArchivedFileVersion;
import opa.rmi_interfaces.IFileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class FileManagerImpl extends UnicastRemoteObject implements IFileManager {

    protected FileManagerImpl() throws RemoteException {
    }

    @Override
    public boolean uploadFile(String username, String filename, String localPath, RemoteInputStream remoteInputStream) {
        InputStream istream = null;
        FileOutputStream ostream = null;

        try {
            istream = RemoteInputStreamClient.wrap(remoteInputStream);
            File directory = findOrCreateDirectory(username, filename, localPath);
            File file = File.createTempFile(getFileNameWithoutExtension(filename), "." + getFileExtension(filename), directory);
                    //createTempFile(filename, directory);
            ostream = new FileOutputStream(file);
            System.out.println("Writing file " + file);

            byte[] buf = new byte[1024];

            int bytesRead;
            while ((bytesRead = istream.read(buf)) >= 0) {
                ostream.write(buf, 0, bytesRead);
            }
            ostream.flush();

            System.out.println("Finished writing file " + file);

            istream.close();
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public RemoteInputStream downloadFile(ArchivedFileVersion archivedFileVersion) throws IOException {
        // create a RemoteStreamServer (note the finally block which only releases
        // the RMI resources if the method fails before returning.)
        RemoteInputStreamServer istream = null;
        try {
            istream = new GZIPRemoteInputStream(new BufferedInputStream(
                    new FileInputStream(archivedFileVersion.getFilePath())));
            // export the final stream for returning to the client
            RemoteInputStream result = istream.export();
            // after all the hard work, discard the local reference (we are passing
            // responsibility to the client)
            istream = null;
            return result;
        } finally {
            // we will only close the stream here if the server fails before
            // returning an exported stream
            if(istream != null) istream.close();
        }
    }

    private String getFileNameWithoutExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }

    private String getFileExtension(String filename) {
       String[] file = filename.split("\\.");
       if(file[file.length - 1] != null)
           return file[file.length - 1];

       return "";
    }

    private File createTempFile(String filename, File directory) throws IOException {
        File file = new File(directory + "/" + filename);
        file.createNewFile();
        return file;
    }

    private File findOrCreateDirectory(String username, String filename, String localPath) throws IOException {
        String path = "users//" + username + "//" + filename;
        File directory = new File(path);
        if (directory.exists())
            return directory;

        directory.mkdir();
        File activeFileTxt = new File(path + "//active.txt");
        activeFileTxt.createNewFile();
        DataOutputStream toFile = new DataOutputStream(new FileOutputStream(activeFileTxt));
        toFile.writeBytes("1");
        toFile.close();
        File pathFileTxt = new File(path + "//path.txt");
        toFile = new DataOutputStream(new FileOutputStream(pathFileTxt));
        toFile.writeBytes(localPath);
        toFile.close();
        return directory;
    }

    @Override
    public ArrayList<ArchivedFile> getArchivedFiles(String username) {
        ArrayList<ArchivedFile> observableList = new ArrayList<>();
        String path = "users//" + username;

        File[] directories = new File(path).listFiles();
        for (File directory : directories) {
            if (directory.getName().equals("password.txt"))
                continue;
            ArchivedFile archivedFile = null;
            ArrayList<ArchivedFileVersion> archivedFileVersions = new ArrayList<>();
            File[] fileVersions = directory.listFiles();

            for (File fileVersion : fileVersions) {
                if (fileVersion.getName().equals("active.txt") || fileVersion.getName().equals("path.txt"))
                    continue;
                BasicFileAttributes attributes = null;
                try {
                    attributes = Files.readAttributes(fileVersion.toPath(), BasicFileAttributes.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArchivedFileVersion archivedFileVersion = new ArchivedFileVersion(fileVersion.getPath(), directory.getName(), attributes.lastModifiedTime().toString());
                archivedFileVersions.add(archivedFileVersion);
            }


            try {
                archivedFile = new ArchivedFile(directory.getName(), getFilePathClientSide(directory.getPath()), checkIfArchivingIsEnabled(directory.getPath()), archivedFileVersions);
            } catch (IOException e) {
                e.printStackTrace();
            }
            observableList.add(archivedFile);
        }
        return observableList;
    }

    private String getFilePathClientSide(String filePath) throws IOException {
        File params = new File(filePath + "//path.txt");
        BufferedReader reader = new BufferedReader(new FileReader(params));

        return reader.readLine();
    }

    private boolean checkIfArchivingIsEnabled(String filePath) throws IOException {
        File params = new File(filePath + "//active.txt");
        BufferedReader reader = new BufferedReader(new FileReader(params));
        if (reader.readLine().equals("1"))
            return true;

        return false;
    }

}
