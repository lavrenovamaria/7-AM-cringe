package core.domain.models;

//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.File;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DriveApiClientImpl implements DriveApiClient {
    private static final String CREDENTIALS_FILE_PATH = "client_secret_622097366523-9m4v33a80bglu0ah7v3bdlgkdtlkgmnp.apps.googleusercontent.com";
    private static final String PICTURES_FOLDER_ID = "1qAB2GzEa9Il_hYDjb1ddJ2kUhDQDAGmv";

    private GoogleCredentials credentials;
    private Drive drive;

    public DriveApiClientImpl() throws IOException, GeneralSecurityException {
        this.credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH));
        this.credentials = this.credentials.createScoped(Collections.singleton(DriveScopes.DRIVE)); // Add the necessary scopes
        this.drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(this.credentials) // Use HttpCredentialsAdapter here
        ).setApplicationName("Your Application Name").build();
    }

    public List<File> getImagesFromDrive() throws IOException {
        // Create a query to filter files by the folder ID
        String query = "'" + PICTURES_FOLDER_ID + "' in parents";

        // Get the list of all the pictures in the folder using the query
        FileList fileList = drive.files().list().setQ(query).execute();
        return fileList.getFiles();
    }
}