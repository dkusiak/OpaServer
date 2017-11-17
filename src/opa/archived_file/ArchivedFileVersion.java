package opa.archived_file;

import java.io.Serializable;

public class ArchivedFileVersion implements Serializable{
    private String filePath;
    private String archivedDate;
    private String filename;

    public ArchivedFileVersion(String filePath, String filename, String archivedDate) {
        this.filePath = filePath;
        this.archivedDate = archivedDate;
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getArchivedDate() {
        return archivedDate.substring(0, 10) + "_" + archivedDate.substring(11, 16).replace(":", "-");
    }

    public void setArchivedDate(String archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
