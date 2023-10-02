package core.domain.models;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.io.InputStream;

public class DriveApiClientImpl implements DriveApiClient {
    private static final String PICTURES_FOLDER_ID = "1qAB2GzEa9Il_hYDjb1ddJ2kUhDQDAGmv";

    private GoogleCredentials credentials;
    private Drive drive;

    public DriveApiClientImpl(String filePath) throws IOException, GeneralSecurityException {

        InputStream credentialsStream = new FileInputStream(filePath);
        this.credentials = GoogleCredentials.fromStream(credentialsStream);
        this.credentials = this.credentials.createScoped(Collections.singleton(DriveScopes.DRIVE));

        this.drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(this.credentials)
        ).setApplicationName("Your Application Name").build();
    }

    public List<File> getImagesFromDrive() throws IOException {

        String query = "'" + PICTURES_FOLDER_ID + "' in parents";

        FileList fileList = drive.files().list().setQ(query).execute();
        return fileList.getFiles();
    }

    public void downloadFileContent(String fileId, OutputStream outputStream) throws IOException {
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
    }
}