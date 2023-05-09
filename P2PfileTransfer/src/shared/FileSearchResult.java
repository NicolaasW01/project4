package shared;

public class FileSearchResult {
    private String fileName;
    private long fileSize;
    private String clientId;

    // Constructor
    public FileSearchResult(String fileName, long fileSize, String clientId) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.clientId = clientId;
    }

    // Getters
    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getClientId() {
        return clientId;
    }

    // Setters
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
