package core.domain.models;

import java.io.InputStream;

public class DriveFile {
    private String fileName;
    private InputStream inputStream;

    public DriveFile(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
