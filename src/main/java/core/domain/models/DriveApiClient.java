package core.domain.models;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface DriveApiClient {
    List<File> getImagesFromDrive() throws IOException;
    void downloadFileContent(String fileId, OutputStream outputStream) throws IOException;
}
