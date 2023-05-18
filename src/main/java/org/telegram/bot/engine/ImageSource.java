package org.telegram.bot.engine;

import java.io.File;
import java.util.List;

// Too lazy to clean it up, hope you've got the idea of ImageSource
class GoogleDriveAdapter implements ImageSource {

    void init() {
        String fileName = "reference-unity-383120-97164bb76379.json";
        String workingDirectory = System.getProperty("user.dir");
        String filePath = workingDirectory + File.separator + fileName;

        GoogleDriveService googleDriveService = new GoogleDriveService(filePath);
        String folderName = "cringe";
        String folderId = googleDriveService.getFolderIdByName(folderName);

        if (folderId == null) {
            throw new RuntimeException("No folder with %s name".formatted(folderName));
        }
        List<String> imageUrls = googleDriveService.getImageUrlsFromFolder(folderId);
    }

    @Override
    public String getNext() {
        return null;
    }
}
