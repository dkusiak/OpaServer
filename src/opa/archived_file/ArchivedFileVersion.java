package opa.archived_file;

import java.io.Serializable;

public class ArchivedFileVersion implements Serializable{
    private String filePath;
    private String archivedDate;

    public ArchivedFileVersion(String filePath, String archivedDate) {
        this.filePath = filePath;
        this.archivedDate = archivedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(String archivedDate) {
        this.archivedDate = archivedDate;
    }
}
