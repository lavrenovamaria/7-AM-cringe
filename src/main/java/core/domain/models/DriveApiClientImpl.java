package core.domain.models;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

public class DriveApiClientImpl implements DriveApiClient {
    private static final String CREDENTIALS_FILE_PATH = "client_secret_622097366523-9m4v33a80bglu0ah7v3bdlgkdtlkgmnp.apps.googleusercontent.com";
    private static final String PICTURES_FOLDER_ID = "1qAB2GzEa9Il_hYDjb1ddJ2kUhDQDAGmv";

    private GoogleCredentials credentials;
    private Drive drive;

    public DriveApiClientImpl() throws IOException, GeneralSecurityException {
        this.credentials = GoogleCredentials.fromStream(new File(CREDENTIALS_FILE_PATH));
        this.drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credentials
        ).build();
    }

    public List<File> getImagesFromDrive() throws IOException {
        // Get the list of all the pictures in the folder.
        FileList fileList = drive.files().list(PICTURES_FOLDER_ID).execute();
        return fileList.getFiles();
    }
}