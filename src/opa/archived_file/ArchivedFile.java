package opa.archived_file;

import java.io.Serializable;
import java.util.ArrayList;

public class ArchivedFile implements Serializable{
    private String fileName;
    private boolean isArchived;
    private String filePath;
    private ArrayList<ArchivedFileVersion> archivedFileVersionList;

    public ArchivedFile(String fileName, String filePath, boolean isArchived, ArrayList<ArchivedFileVersion> archivedFileVersionList) {
        this.fileName = fileName;
        this.isArchived = isArchived;
        this.filePath = filePath;
        this.archivedFileVersionList = archivedFileVersionList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean getIsArchived() {
        return isArchived;
    }

    public ArrayList<ArchivedFileVersion> getArchivedFileVersionList() {
        return archivedFileVersionList;
    }

    public void setArchivedFileVersionList(ArrayList<ArchivedFileVersion> archivedFileVersionList) {
        this.archivedFileVersionList = archivedFileVersionList;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
